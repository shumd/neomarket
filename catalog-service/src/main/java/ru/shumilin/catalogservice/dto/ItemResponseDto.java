package ru.shumilin.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemResponseDto(

        String id,

        String name,

        String description,

        BigDecimal price,

        @JsonProperty("id_category")
        String categoryId,

        @JsonProperty("id_org_supplier")
        String orgSupplierId,

        Integer quantity
) {
}
