package ru.tataev.basket_service.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.AddRequestDto;
import ru.tataev.basket_service.dto.AddResponseDto;
import ru.tataev.basket_service.dto.ItemDto;
import ru.tataev.basket_service.entity.*;
import ru.tataev.basket_service.exception.InvalidRequestException;
import ru.tataev.basket_service.exception.ResourceNotFoundException;
import ru.tataev.basket_service.repository.OrderItemRepository;
import ru.tataev.basket_service.repository.OrderRepository;
import ru.tataev.basket_service.repository.StatusRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final OrderItemRepository orderItemRepository;

    public AddResponseDto addToBasket(AddRequestDto req) {
        if (req == null) {
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        validateIdUser(req.getIdUser());

        Order order;
        if (isBasketExist(req.getIdUser())) {
            order = findBasketByUser(req.getIdUser());

            Status successOrderStatus = statusRepository.findById(OrderStatus.DRAFT.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Статус " + OrderStatus.DRAFT.getName() + " не найден"));
            order.setStatus(successOrderStatus);

            order.setTotalAmount(order.getTotalAmount().add(req.getInitialPrice()));
            order.setTotalQuantity(order.getTotalQuantity() + req.getCount());
        } else {
            order = createOrder(req);
        }

        order = orderRepository.save(order);

        OrderItem orderItem = new OrderItem();

        Status successOrderItemStatus = statusRepository.findById(ItemStatus.ORDER_ITEM_PENDING.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Статус " + ItemStatus.ORDER_ITEM_PENDING.getName() + " не найден"));
        orderItem.setStatus(successOrderItemStatus);

        orderItem.setOrder(order);
        orderItem.setQuantity(req.getCount());
        orderItem.setInitialPrice(req.getInitialPrice());
        orderItem.setDiscount(req.getDiscount());

        orderItem = orderItemRepository.save(orderItem);

        return mapToDto(order, orderItem);
    }

    private Order createOrder(AddRequestDto req){
        Order order = new Order();
        order.setIdUser(req.getIdUser());
        order.setAddress(""); //TODO: уточнить адрес
        order.setTotalQuantity(req.getCount());
        order.setTotalAmount(req.getInitialPrice());
        order.setDateOrder(LocalDate.now());

        Status successStatus = statusRepository.findById(OrderStatus.DRAFT.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Статус " + OrderStatus.DRAFT.getName() + " не найден"));
        order.setStatus(successStatus);

        return order;
    }

    public boolean isBasketExist(UUID idUser) {
        return orderRepository.existsByIdUser(idUser);
    }

    public Order findBasketByUser(UUID idUser) {
        Order order = orderRepository.findByIdUser(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ этого пользователя не найден"));

        return order;
    }

    private void validateIdUser(UUID idUser) {
        if (idUser == null) {
            throw new InvalidRequestException("id_user не может быть пустым");
        }

        if (idUser.version() != 4) {
            throw new InvalidRequestException("id_user должен быть версии UUID v4");
        }
    }

    private AddResponseDto mapToDto(Order order, OrderItem orderItem){
        AddResponseDto res = new AddResponseDto();

        res.setId(String.valueOf(order.getId()));
        res.setAddress(order.getAddress());
        res.setTotalAmount(order.getTotalAmount());
        res.setTotalQuantity(order.getTotalQuantity());
        res.setItemStatus(String.valueOf(orderItem.getStatus().getId()));
        res.setBasketStatus(String.valueOf(order.getStatus().getId()));
        res.setDateOrder(order.getDateOrder());
        res.setIdShipping(null); //TODO: разаработать сущность Shipping
        res.setIdUser(order.getIdUser());
        res.setIdComission(null); //TODO: разаработать сущность Comission

        List<ItemDto> items = new ArrayList<>();
        ItemDto newItem = new ItemDto();
        newItem.setId(orderItem.getId());
        newItem.setCount(orderItem.getQuantity());
        items.add(newItem);
        res.setIdItem(items);

        return res;
    }
}
