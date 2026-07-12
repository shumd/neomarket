package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("address")
    private String address;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("total_quantity")
    private Integer totalQuantity;

    @JsonProperty("status")
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("date_order")
    private LocalDate dateOrder;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("id_shipping")
    private String idShipping;

    @JsonProperty("id_user")
    private UUID idUser;

    @JsonProperty("id_comission")
    private String idComission;

    @JsonProperty("id_item")
    private List<ItemDto> idItem;
}
