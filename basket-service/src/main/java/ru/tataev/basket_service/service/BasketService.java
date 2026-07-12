package ru.tataev.basket_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.BuyResponseDto;
import ru.tataev.basket_service.dto.BuyRequestDto;
import ru.tataev.basket_service.dto.ItemDto;
import ru.tataev.basket_service.entity.Order;
import ru.tataev.basket_service.entity.OrderStatus;
import ru.tataev.basket_service.entity.Status;
import ru.tataev.basket_service.exception.InvalidRequestException;
import ru.tataev.basket_service.exception.ResourceNotFoundException;
import ru.tataev.basket_service.repository.OrderRepository;
import ru.tataev.basket_service.repository.StatusRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;

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

    private void validateStatus(Long idStatus){
        if (!OrderStatus.DRAFT.getId().equals(idStatus)){
            throw new InvalidRequestException("Недопустимый статус: " + idStatus);
        }
    }
}
