package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Запрос на добавление товара в корзину")
@Data
public class AddRequestDto {
    @Schema(description = "id товара", example = "1026", minimum = "1", maximum = "999999999")
    @JsonProperty("id_item")
    @NotNull(message = "id_item не может быть пустым")
    @Min(value = 1, message = "id_item должен быть >= 1")
    @Max(value = 999999999, message = "id_item должен быть <= 999999999")
    private Long idItem;

    @Schema(description = "id пользователя, в чью корзину добавляется товар",
            example = "a917227f-08fb-4c1b-bf0c-e0ad7fa227fa", format = "uuid")
    @JsonProperty("id_user")
    @NotNull(message = "id_user не может быть пустым")
    private UUID idUser;

    @Schema(description = "Колмчество товара", example = "12", minimum = "1", maximum = "99")
    @JsonProperty("count")
    @NotNull(message = "count не может быть пустым")
    @Max(value = 99, message = "count должен быть не более 99")
    @Min(value = 1, message = "count должен быть больше 0")
    private Integer count;

    @Schema(description = "Стоимость товара", example = "2054.35", minimum = "1.00", maximum = "999999999.99")
    @JsonProperty("initial_price")
    @NotNull(message = "initial_price не может быть пустым")
    @DecimalMin(value = "1.0", message = "initial_price должен быть >= 1")
    @DecimalMax(value = "999999999.99", message = "initial_price не должен превышать 999 999 999.99")
    @Digits(integer = 9, fraction = 2, message = "initial_price не может иметь более 2 знаков после запятой")
    private BigDecimal initialPrice;

    @Schema(description = "Персональная скидка на товар", example = "0", minimum = "0", maximum = "100")
    @JsonProperty("discount")
    @NotNull(message = "discount не может быть пустым")
    @Max(value = 100, message = "discount не должен превышать 100")
    @Min(value = 0, message = "discount должен быть больше или равен 0")
    private Integer discount;
}
