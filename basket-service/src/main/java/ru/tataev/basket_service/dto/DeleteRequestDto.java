package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class DeleteRequestDto {
    @JsonProperty("id_order")
    @NotNull(message = "id_order не может быть пустым")
    @Min(value = 1, message = "id_order должен быть >= 1")
    @Max(value = 999999999, message = "id_order должен быть <= 999999999")
    private Long idOrder;

    @JsonProperty("items")
    @NotNull(message = "items должен присутствовать в запросе")
    @Size(max = 100, message = "длина items должна быть <= 100")
    private List<ItemDto> items;
}
