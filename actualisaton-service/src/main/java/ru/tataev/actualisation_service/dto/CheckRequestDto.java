package ru.tataev.actualisation_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CheckRequestDto {
    @JsonProperty("items")
    @NotNull(message = "items должен присутствовать в запросе")
    @Size(min = 1, max = 99, message = "длина items должна быть >= 1 и <= 100")
    private List<ItemRequestDto> items;

    @JsonProperty("id_warehouse")
    @NotNull(message = "id_warehouse должен присутствовать в запросе")
    @Min(value = 1, message = "id_warehouse должен быть >= 1")
    @Max(value = 999, message = "id_warehouse должен быть <= 999")
    private Long idWarehouse;

    @JsonProperty("date")
    @NotNull(message = "date должен присутствовать в запросе")
    private LocalDate date;

    @JsonProperty("id_user")
    @NotNull(message = "id_user должен присутствовать в запросе")
    private UUID idUser;

    @JsonProperty("id_basket")
    @NotNull(message = "id_basket должен присутствовать в запросе")
    @Min(value = 1, message = "id_basket должен быть >= 1")
    @Max(value = 999999999, message = "id_basket должен быть <= 999999999")
    private Long idBasket;

    @JsonProperty("id_status")
    @NotNull(message = "id_status должен присутствовать в запросе")
    @Min(value = 1, message = "id_status должен быть >= 1")
    @Max(value = 999, message = "id_status должен быть <= 999")
    private Long idStatus;
}
