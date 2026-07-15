package ru.tataev.basket_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Ответ содержащий информацию о добавленном товаре и корзине")
@Data
public class AddResponseDto {
    @Schema(description = "id товара", example = "1024")
    @JsonProperty("id")
    private String id;

    @Schema(description = "Адрес доставки товара", example = "")
    @JsonProperty("address")
    private String address;

    @Schema(description = "Стоимсоть товаров в корзине", example = "2054.35")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @Schema(description = "Количество товаров в корзине", example = "12")
    @JsonProperty("total_quantity")
    private Integer totalQuantity;

    @Schema(description = "Текущий статус товара", example = "01")
    @JsonProperty("item_status")
    private String itemStatus;

    @Schema(description = "Текущий статус заказа", example = "0004")
    @JsonProperty("basket_status")
    private String basketStatus;

    @Schema(description = "Дата отправки заказа клиенту", example = "01")
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
