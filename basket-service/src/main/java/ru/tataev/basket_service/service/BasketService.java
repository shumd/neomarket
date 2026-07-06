package ru.tataev.basket_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.BasketResponseDto;
import ru.tataev.basket_service.dto.CreateRequestDto;
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

    public BasketResponseDto updateBasket(CreateRequestDto req){
        if (req == null){
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        validateIdUser(req.getIdUser());
        validateIdItem(req.getIdItem());
        validateStatus(req.getStatus());

        Order order = orderRepository.findById(Long.parseLong(req.getIdOrder()))
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с id " + req.getIdOrder() + " не найден"));

        order.setIdUser(req.getIdUser());
        order.setAddress(req.getAddress());
        order.setTotalAmount(req.getTotalAmount());

        Status successStatus = statusRepository.findById(3L)
                .orElseThrow(() -> new ResourceNotFoundException("Статус SUCCESS не найден"));
        order.setStatus(successStatus);

        order.setDateOrder(LocalDate.now());

        orderRepository.save(order);

        return mapToDto(order);
    }

    private BasketResponseDto mapToDto(Order order){
        BasketResponseDto res = new BasketResponseDto();
        res.setIdOrder(String.valueOf(order.getId()));
        res.setIdUser(order.getIdUser());
        res.setAddress(order.getAddress());
        res.setTotalAmount(order.getTotalAmount());
        res.setStatus(order.getStatus().getName());
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

    private void validateIdItem(List<ItemDto> idItem){
        if (idItem == null || idItem.isEmpty()){
            throw new InvalidRequestException("id_item не может быть пустым");
        }

        try {
            long item;
            for (ItemDto itemDto : idItem) {
                item = Long.parseLong(itemDto.getId());
                if (item <= 0) {
                    throw new InvalidRequestException("id_item должен иметь элементы только с положительными id");
                }
            }
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("id_item содержит элементы с нечисловыми id");
        }
    }

    private void validateStatus(String status){
        if (!(OrderStatus.NOT_CREATED.getId().equals(status) || OrderStatus.NOT_PAID.getId().equals(status))){
            throw new InvalidRequestException("Недопустимый статус: " + status);
        }
    }
}
