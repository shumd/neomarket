package ru.tataev.basket_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.ItemDto;
import ru.tataev.basket_service.dto.OrderResponseDto;
import ru.tataev.basket_service.entity.Order;
import ru.tataev.basket_service.entity.OrderItem;
import ru.tataev.basket_service.exception.InvalidRequestException;
import ru.tataev.basket_service.exception.ResourceNotFoundException;
import ru.tataev.basket_service.repository.OrderItemRepository;
import ru.tataev.basket_service.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderResponseDto getBasketById(String reqId){
        long id = convertIdToLong(reqId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с id: {" + id + "} не найден"));

        List<OrderItem> items = orderItemRepository.findByOrder_Id(id);

        return mapToDto(order, items);
    }

    public OrderResponseDto mapToDto(Order order, List<OrderItem> items){
        OrderResponseDto res = new OrderResponseDto();
        res.setId(String.valueOf(order.getId()));
        res.setAddress(order.getAddress());
        res.setTotalAmount(order.getTotalAmount());
        res.setTotalQuantity(order.getTotalQuantity());
        res.setStatus(String.format("%04d", order.getStatus().getId()));
        res.setDateOrder(order.getDateOrder());
        res.setComment(order.getComment());
        res.setIdShipping(null); //TODO: разаработать сущность Shipping
        res.setIdUser(order.getIdUser());
        res.setIdComission(null); //TODO: разаработать сущность Comission

        List<ItemDto> itemsDto = new ArrayList<>();
        for (OrderItem item: items){
            ItemDto itemDto = new ItemDto();
            itemDto.setId(item.getId());
            itemDto.setCount(item.getQuantity());

            itemsDto.add(itemDto);
        }
        res.setIdItem(itemsDto);

        return res;
    }

    public Long convertIdToLong(String id){
        if (id == null || id.isBlank() || id.length() > 36){
            throw new InvalidRequestException("id должен быть длиной <= 36 символов");
        }
        try {
            long numId = Long.parseLong(id);
            if (numId <= 0){
                throw new InvalidRequestException("id должен быть пположительным");
            }
            return numId;
        } catch (NumberFormatException ex){
            throw new InvalidRequestException("id должен быть числом");
        }
    }
}
