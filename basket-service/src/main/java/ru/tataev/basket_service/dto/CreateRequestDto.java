package ru.tataev.basket_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CreateRequestDto {
    private String id_order;
    private UUID id_user;
    private BigDecimal total_amount;
    private List<ItemDto> id_item;
    private String status;
}


