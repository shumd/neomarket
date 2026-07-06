package ru.shumilin.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Ответ с товаром и наименованием поставщика")
public record ItemWithSupplierResponseDto(

        @Schema(description = "Идентификатор товара", example = "1")
        String id,

        @Schema(description = "Наименование товара", example = "Смартфон Neo Pro")
        String name,

        @Schema(description = "Текущая цена товара", example = "49999")
        BigDecimal price,

        @Schema(description = "Наименование поставщика", example = "Alpha Transport")
        @JsonProperty("supplier_name")
        String supplierName,

        @Schema(description = "Количество на складе", example = "15")
        int quantity
) {
}
