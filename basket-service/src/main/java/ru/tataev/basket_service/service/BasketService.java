package ru.tataev.basket_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.*;
import ru.tataev.basket_service.entity.Order;
import ru.tataev.basket_service.entity.OrderStatus;
import ru.tataev.basket_service.entity.Status;
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

    public BuyResponseDto updateBasket(BuyRequestDto req){
        if (req == null){
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        validateIdUser(req.getIdUser());

        Order order = orderRepository.findById(req.getIdOrder())
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с id " + req.getIdOrder() + " не найден"));

        validateStatus(order.getStatus().getId());

        order.setIdUser(req.getIdUser());
        order.setAddress(req.getAddress());
        // TODO: пересчитывать totalAmount на основе цен товаров, а не брать из запроса
        order.setTotalAmount(req.getTotalAmount());

        Status successStatus = statusRepository.findById(OrderStatus.ORDER_CONFIRMED.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Статус " + OrderStatus.ORDER_CONFIRMED.getName() + " не найден"));
        order.setStatus(successStatus);

        order.setDateOrder(LocalDate.now());

        orderRepository.save(order);

        //TODO: загружать idItem из БД
        return mapToDto(order, req.getIdItem());
    }

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

    public OrderResponseDto getBasketById(String reqId){
        long id = convertIdToLong(reqId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с id: {" + id + "} не найден"));

        List<OrderItem> items = orderItemRepository.findByOrder_Id(id);

        return mapToOrderResponseDto(order, items);
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

    public OrderResponseDto mapToOrderResponseDto(Order order, List<OrderItem> items){
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

    private BuyResponseDto mapToDto(Order order, List<ItemDto> idItem){
        BuyResponseDto res = new BuyResponseDto();
        res.setIdOrder(String.valueOf(order.getId()));
        res.setIdUser(order.getIdUser());
        res.setAddress(order.getAddress());
        res.setTotalAmount(order.getTotalAmount());
        res.setStatus(order.getStatus().getName());
        res.setIdItem(idItem);
        res.setDateOrder(order.getDateOrder());
        return res;
    }

    private void validateIdUser(UUID idUser){
        if (idUser == null){
            throw new InvalidRequestException("id_user не может быть пустым");
        }

        if (idUser.version() != 4){
            throw new InvalidRequestException("id_user должен быть версии UUID v4");
        }
    }

    private void validateStatus(Long idStatus) {
        if (!OrderStatus.DRAFT.getId().equals(idStatus)) {
            throw new InvalidRequestException("Недопустимый статус: " + idStatus);
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
