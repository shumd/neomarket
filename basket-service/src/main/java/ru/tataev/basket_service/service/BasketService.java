package ru.tataev.basket_service.service;

import org.springframework.stereotype.Service;
import ru.tataev.basket_service.dto.BasketResponseDto;
import ru.tataev.basket_service.dto.CreateRequestDto;
import ru.tataev.basket_service.exception.InvalidRequestException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class BasketService {
    public BasketResponseDto createBasket(CreateRequestDto req){
        if (req == null){
            throw new InvalidRequestException("Пришел пустой запрос");
        }

        validateIdUser(req.getIdUser());
        validateTotalAmount(req.getTotalAmount());

        BasketResponseDto res = new BasketResponseDto();
        res.setId_order(req.getIdOrder());
        res.setId_user(req.getIdUser());
        res.setTotal_amount(req.getTotalAmount());
        res.setId_item(req.getIdItem());
        res.setStatus("Успешно");
        res.setDate_order(LocalDate.now());
        return res;
    }

    private void validateIdUser(UUID idUser){
        if (idUser.version() != 4){
            throw new InvalidRequestException("id_user должен быть версии UUID v4");
        }
    }

    private void validateTotalAmount(BigDecimal totalAmount){
        if (totalAmount.scale() > 2){
            throw new InvalidRequestException("totalAmount должен иметь не более 2 знаков после запятой");
        }
    }
}
