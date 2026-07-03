package ru.shumilin.catalogservice.dto;

import java.util.List;

public record CategoryListResponseDto(
        List<CategoryResponseDto> categories
) {
}
