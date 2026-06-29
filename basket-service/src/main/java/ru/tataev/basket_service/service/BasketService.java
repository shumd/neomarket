package ru.tataev.basket_service.service;

import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.BasketResponseDto;
import ru.tataev.basket_service.dto.CreateRequestDto;
import ru.tataev.basket_service.dto.ItemDto;
import ru.tataev.basket_service.exception.InvalidRequestException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BasketService {
    public BasketResponseDto createBasket(CreateRequestDto req){
        validateIdOrder(req.getId_order());
        validateIdUser(req.getId_user());
        validateTotalAmount(req.getTotal_amount());
        validateIdItem(req.getId_item());
        validateStatus(req.getStatus());

        BasketResponseDto res = new BasketResponseDto();
        res.setId_order(req.getId_order());
        res.setId_user(req.getId_user());
        res.setTotal_amount(req.getTotal_amount());
        res.setId_item(req.getId_item());
        res.setStatus("Оформлен");
        res.setDate_order(LocalDateTime.now().toString());
        return res;
    }

    private void validateIdOrder(String idOrder){
        if (idOrder == null || idOrder.isBlank()){
            throw new InvalidRequestException("id_order не может быть пустым");
        }

        long idOrderInt;
        try{
            idOrderInt = Long.parseLong(idOrder);
        } catch (NumberFormatException e){
            throw new InvalidRequestException("id_order должен быть целым положительным числом");
        }

        if (idOrderInt < 1 || idOrderInt > 999999999){
            throw new InvalidRequestException("id_order должен быть от 1 до 999 999 999");
        }
    }

    private void validateIdUser(UUID idUser){
        if (idUser == null){
            throw new InvalidRequestException("id_user не может быть пустым");
        }
        if (idUser.version() != 4){
            throw new InvalidRequestException("id_user должен быть версии UUID v4");
        }
    }

    private void validateTotalAmount(BigDecimal totalAmount){
        if (totalAmount == null){
            throw new InvalidRequestException("total_amount не может быть пустым");
        }
        if (totalAmount.compareTo(BigDecimal.ONE) < 0){
            throw new InvalidRequestException("total_amount должен быть положительным");
        }
        if (totalAmount.compareTo(new BigDecimal("999999999.99")) > 0){
            throw new InvalidRequestException("total_amount не должен превышать 999 999 999.99");
        }
    }

    private void validateIdItem(List<ItemDto> idItem){
        if (idItem == null || idItem.isEmpty()){
            throw new InvalidRequestException("id_item не может быть пустым");
        }
        if (idItem.size() > 10){
            throw new InvalidRequestException("id_item должен вмещать максимум 10 элементов");
        }
        ItemDto item;
        for (int i = 0; i < idItem.size(); i++){
            item = idItem.get(i);
            if (item.getId() == null || item.getId().isBlank()){
                throw new InvalidRequestException("id_item["+i+"].id не может быть пустым");
            }
            if (item.getCount() == null || item.getCount() <= 0){
                throw new InvalidRequestException("id_count["+i+"].id должен быть положительным числом");
            }
        }
    }

    private void validateStatus(String status){
        if (status == null || status.isBlank()) {
            throw new InvalidRequestException("status не может быть пустым");
        }
        if (!status.equals("Не оформлен") && !status.equals("Не оплачен")) {
            throw new InvalidRequestException("status должен быть 'Не оформлен' или 'Не оплачен'");
        }
    }
}
