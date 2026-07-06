package ru.shumilin.catalogservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ со списком товаров и пагинацией")
public record ItemPageResponseDto (

        @Schema(description = "Список товаров")
        List<ItemWithSupplierResponseDto> items,

        @Schema(description = "Лимит на страницу", example = "20")
        int size,

        @Schema(description = "Текущее смещение", example = "0")
        int page
){
}
