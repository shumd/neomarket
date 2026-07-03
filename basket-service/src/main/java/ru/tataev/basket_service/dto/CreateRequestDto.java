package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CreateRequestDto {
    @JsonProperty("id_order")
    @NotBlank(message = "id_order не может быть пустым")
    @Size(max = 36, message = "id_order имеет длину, превышающую 36 символов")
    @Pattern(regexp = "^\\d+$", message = "id_order должен содержать только цифры")
    private String idOrder;

    @JsonProperty("id_user")
    @NotNull(message = "id_user не может быть пустым")
    private UUID idUser;

    @JsonProperty("total_amount")
    @NotNull(message = "total_amount не может быть пустым")
    @DecimalMin(value = "1.0", message = "total_amount должен быть >= 1")
    @DecimalMax(value = "999999999.99", message = "total_amount не должен превышать 999 999 999.99")
    private BigDecimal totalAmount;

    @JsonProperty("id_item")
    @NotEmpty(message = "id_item не может быть пустым")
    @Size(max = 10, message = "id_item должен вмещать максимум 10 элементов")
    @Valid
    private List<ItemDto> idItem;

    @JsonProperty("status")
    @NotBlank(message = "status не может быть пустым")
    @Pattern(regexp = "^(Не оформлен|Не оплачен)$")
    private String status;
}


