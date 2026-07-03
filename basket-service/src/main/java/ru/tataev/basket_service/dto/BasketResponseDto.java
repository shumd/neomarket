package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BasketResponseDto {
    private String id_order;
    private UUID id_user;
    private BigDecimal total_amount;
    private String status;
    private List<ItemDto> id_item;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date_order;
}
