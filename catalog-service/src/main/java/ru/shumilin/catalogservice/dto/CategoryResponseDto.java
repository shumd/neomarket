package ru.shumilin.catalogservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с категорией товара")
public record CategoryResponseDto(

        @Schema(description = "Идентификатор категории", example = "1")
        String id,

        @Schema(description = "Наименование категории", example = "Электроника")
        String name
) {
}
