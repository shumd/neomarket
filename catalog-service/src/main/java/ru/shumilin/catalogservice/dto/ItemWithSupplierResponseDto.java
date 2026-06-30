package ru.shumilin.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemWithSupplierResponseDto(
        String id,
        String name,
        int price,
        @JsonProperty("supplier_name")
        String supplierName,
        int quantity
) {
}
