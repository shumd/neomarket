package ru.shumilin.catalogservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ со списком категорий")
public record CategoryListResponseDto(

        @Schema(description = "Список категорий")
        List<CategoryResponseDto> categories
) {
}
