package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemDto {
    @JsonProperty("id")
    @NotNull(message = "id_item не должен иметь элементы с пустым id")
    private String id;

    @JsonProperty("count")
    @NotNull(message = "id_item не должен иметь элементы с пустым count")
    @Positive(message = "id_item должен иметь элементы только с положительным count")
    private Integer count;
}
