package ru.shumilin.catalogservice.dto;

import java.util.List;

public record ItemPageResponseDto (
        List<ItemWithSupplierResponseDto> items,
        int size,
        int page
){
}
