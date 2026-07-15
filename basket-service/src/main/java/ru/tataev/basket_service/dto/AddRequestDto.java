package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddRequestDto {
    @JsonProperty("id_item")
    @NotNull(message = "id_item не может быть пустым")
    @Min(value = 1, message = "id_item должен быть >= 1")
    @Max(value = 999999999, message = "id_item должен быть <= 999999999")
    private Long idItem;

    @JsonProperty("id_user")
    @NotNull(message = "id_user не может быть пустым")
    private UUID idUser;

    @JsonProperty("count")
    @NotNull(message = "count не может быть пустым")
    @Max(value = 99, message = "count должен быть не более 99")
    @Min(value = 1, message = "count должен быть больше 0")
    private Integer count;

    @JsonProperty("initial_price")
    @NotNull(message = "initial_price не может быть пустым")
    @DecimalMin(value = "1.0", message = "initial_price должен быть >= 1")
    @DecimalMax(value = "999999999.99", message = "initial_price не должен превышать 999 999 999.99")
    @Digits(integer = 9, fraction = 2, message = "initial_price не может иметь более 2 знаков после запятой")
    private BigDecimal initialPrice;

    @JsonProperty("discount")
    @NotNull(message = "discount не может быть пустым")
    @Max(value = 100, message = "count должен быть не более 99")
    @Min(value = 1, message = "count должен быть больше 0")
    private Integer discount;
}
