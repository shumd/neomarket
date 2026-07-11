package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddRequestDto {
    @JsonProperty("id_order")
    @NotBlank(message = "id_order не может быть пустым")
    @Size(max = 36, message = "id_order имеет длину, превышающую 36 символов")
    private String idOrder;

    @JsonProperty("id_item")
    @NotBlank(message = "id_order не может быть пустым")
    @Size(max = 36, message = "id_order имеет длину, превышающую 36 символов")
    private String idItem;

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
    @Digits(integer = 10, fraction = 2, message = "initial_price не может иметь более 2 знаков после запятой")
    private BigDecimal initialPrice;

    @JsonProperty("discount")
    @NotNull(message = "discount не может быть пустым")
    @Max(value = 100, message = "count должен быть не более 99")
    @Min(value = 1, message = "count должен быть больше 0")
    private Integer discount;

    @JsonProperty("item_status")
    @NotNull(message = "discount не может быть пустым")
    private String itemStatus;
}
