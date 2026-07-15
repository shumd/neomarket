package ru.tataev.actualisation_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemResponseDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("item_status")
    private String status;
}
