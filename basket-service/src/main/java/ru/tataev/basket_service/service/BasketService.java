package ru.tataev.basket_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.DeleteRequestDto;
import ru.tataev.basket_service.dto.DeleteResponseDto;
import ru.tataev.basket_service.dto.ItemDto;
import ru.tataev.basket_service.entity.Order;
import ru.tataev.basket_service.entity.OrderItem;
import ru.tataev.basket_service.exception.InvalidRequestException;
import ru.tataev.basket_service.exception.ResourceNotFoundException;
import ru.tataev.basket_service.repository.OrderItemRepository;
import ru.tataev.basket_service.repository.OrderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public DeleteResponseDto deleteItemsFromBasket(DeleteRequestDto req){
        if (req == null){
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        Order order = orderRepository.findById(req.getIdOrder())
                .orElseThrow(()-> new ResourceNotFoundException("Заказ с id " + req.getIdOrder() + " не найден"));

        if (req.getItems().isEmpty()){
            order.setTotalAmount(BigDecimal.ZERO);
            order.setTotalQuantity(0);
            orderItemRepository.deleteByOrder_id(req.getIdOrder());
        }
        else {
            for (ItemDto itemDto: req.getItems()){
                OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemDto.getId(), req.getIdOrder())
                        .orElseThrow(()-> new ResourceNotFoundException("Товар с id " + itemDto.getId() + " не найден в заказе с id " + req.getIdOrder()));

                if (itemDto.getCount() >= orderItem.getQuantity()){
                    order.setTotalAmount(order.getTotalAmount().subtract(orderItem.getInitialPrice()));
                    order.setTotalQuantity(order.getTotalQuantity() - orderItem.getQuantity());
                    orderItemRepository.delete(orderItem);
                }
                else {
                    BigDecimal deltaPrice = orderItem.getInitialPrice()
                            .divide(BigDecimal.valueOf(orderItem.getQuantity()), 2, RoundingMode.HALF_UP);
                    orderItem.setInitialPrice(orderItem.getInitialPrice().subtract(deltaPrice));
                    orderItem.setQuantity(orderItem.getQuantity() - itemDto.getCount());
                    orderItemRepository.save(orderItem);

                    order.setTotalAmount(order.getTotalAmount().subtract(deltaPrice));
                    order.setTotalQuantity(order.getTotalQuantity() - itemDto.getCount());
                }
            }
        }
        orderRepository.save(order);
        return mapToDto(order);
    }

    public DeleteResponseDto mapToDto(Order order){
        DeleteResponseDto res = new DeleteResponseDto();
        res.setIdOrder(String.valueOf(order.getId()));

        List<OrderItem> items = orderItemRepository.findByOrder_id(order.getId());
        List<ItemDto> itemsToSend = new ArrayList<>();
        for (OrderItem i: items){
            ItemDto itemToSend = new ItemDto();
            itemToSend.setId(i.getId());
            itemToSend.setCount(i.getQuantity());
            itemsToSend.add(itemToSend);
        }
        res.setDifferentItems(itemsToSend);
        res.setTotalAmount(order.getTotalAmount());
        res.setTotalQuantity(order.getTotalQuantity());
        return res;
    }
}
