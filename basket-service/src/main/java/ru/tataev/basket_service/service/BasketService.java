package ru.tataev.basket_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.AddRequestDto;
import ru.tataev.basket_service.dto.AddResponseDto;
import ru.tataev.basket_service.dto.ItemDto;
import ru.tataev.basket_service.entity.Order;
import ru.tataev.basket_service.entity.OrderStatus;
import ru.tataev.basket_service.entity.Status;
import ru.tataev.basket_service.exception.InvalidRequestException;
import ru.tataev.basket_service.exception.ResourceNotFoundException;
import ru.tataev.basket_service.repository.OrderRepository;
import ru.tataev.basket_service.repository.StatusRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;

    public AddResponseDto add(AddRequestDto req){
        validateIdOrder(req.getIdOrder());
        validateIdItem(req.getIdItem());
        validateIdUser(req.getIdUser());
        validateItemStatus(req.getItemStatus());

    }

    public boolean isBasketExist(UUID idUser){
        Order order = orderRepository.f
    }

    public Order getBasket(Long idOrder){
        Order order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с id {" + idOrder + "} не найден"));

        return order;
    }

    public void validateIdOrder(String idOrder){

    }

    public void validateIdItem(String idItem){

    }

    public void validateIdUser(UUID idUser){

    }

    public void validateItemStatus(String itemStatus){

    }
}
