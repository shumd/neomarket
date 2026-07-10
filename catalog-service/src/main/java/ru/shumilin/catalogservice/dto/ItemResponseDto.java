package ru.shumilin.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Ответ с товаром")
public record ItemResponseDto(

        @Schema(description = "Идентификатор товара", example = "1")
        String id,

        @Schema(description = "Наименование товара", example = "Смартфон Neo Pro")
        String name,

        @Schema(description = "Полное описание товара", example = "Смартфон с 6.7-дюймовым экраном...")
        String description,

        @Schema(description = "Текущая цена товара", example = "49999")
        BigDecimal price,

        @Schema(description = "Идентификатор категории товара", example = "5")
        @JsonProperty("id_category")
        String categoryId,

        @Schema(description = "Идентификатор поставщика", example = "12")
        @JsonProperty("id_org_supplier")
        String orgSupplierId,

        @Schema(description = "Количество на складе", example = "15")
        Integer quantity
) {
}
