package ru.shumilin.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemWithSupplierResponseDto(
        String id,
        String name,
        BigDecimal price,
        @JsonProperty("supplier_name")
        String supplierName,
        int quantity
) {
}
