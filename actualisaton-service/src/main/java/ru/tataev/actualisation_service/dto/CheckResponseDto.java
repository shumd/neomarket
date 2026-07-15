package ru.tataev.actualisation_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CheckResponseDto {
    @JsonProperty("items")
    private List<ItemResponseDto> items;

    @JsonProperty("id_warehouse")
    private Long idWarehouse;

    @JsonProperty("date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @JsonProperty("id_user")
    private UUID idUser;

    @JsonProperty("id_basket")
    private Long idBasket;

    @JsonProperty("id_status")
    private Long idStatus;
}
