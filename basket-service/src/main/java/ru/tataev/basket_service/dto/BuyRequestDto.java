package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.tataev.basket_service.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class BuyRequestDto {
    @JsonProperty("id_order")
    @NotNull(message = "id_order не может быть пустым")
    @Min(value = 1, message = "id_order должен быть >= 1")
    @Max(value = 999999999, message = "id_order должен быть <= 999999999")
    private Long idOrder;

    @JsonProperty("address")
    @NotBlank(message = "address не должен быть пустым")
    private String address;

    @JsonProperty("id_user")
    @NotNull(message = "id_user не может быть пустым")
    private UUID idUser;

    @JsonProperty("total_amount")
    @NotNull(message = "total_amount не может быть пустым")
    @DecimalMin(value = "1.0", message = "total_amount должен быть >= 1")
    @DecimalMax(value = "999999999.99", message = "total_amount не должен превышать 999 999 999.99")
    @Digits(integer = 9, fraction = 2, message = "total_amount не может иметь более 2 знаков после запятой")
    private BigDecimal totalAmount;

    @JsonProperty("id_item")
    @NotEmpty(message = "id_item не может быть пустым")
    @Size(max = 10, message = "id_item должен вмещать максимум 10 элементов")
    @Valid
    private List<ItemDto> idItem;
}


