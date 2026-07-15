package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemDto {
    @JsonProperty("id")
    @NotNull(message = "id_item не должен иметь элементы с пустым id")
    @Positive(message = "id_item должен иметь элементы только с положительным id")
    @Min(value = 1, message = "id_item должен быть иметь элементы c id >= 1")
    @Max(value = 999999999, message = "id_item должен быть иметь элементы c id <= 999 999 999")
    private Long id;

    @JsonProperty("count")
    @NotNull(message = "id_item не должен иметь элементы с пустым count")
    @Min(value = 1, message = "id_item должен иметь элементы только с count >= 1")
    @Max(value = 999999999, message = "id_item должен иметь элементы только с count <= 999 999 999")
    private Integer count;
}