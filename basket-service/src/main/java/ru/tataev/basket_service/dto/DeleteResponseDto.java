package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DeleteResponseDto {
    @JsonProperty("id_order")
    private String idOrder;

    @JsonProperty("different_items")
    private List<ItemDto> differentItems;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("total_quantity")
    private Integer totalQuantity;
}
