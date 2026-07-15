package ru.tataev.actualisation_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestDto {
    @JsonProperty("id")
    @NotNull(message = "id_item не должен иметь элементы с пустым id")
    @Min(value = 1, message = "id_item должен быть иметь элементы c id >= 1")
    @Max(value = 999999999, message = "id_item должен быть иметь элементы c id <= 999 999 999")
    private Long id;

    @JsonProperty("count")
    @NotNull(message = "id_item не должен иметь элементы с пустым count")
    private Integer count;
}
