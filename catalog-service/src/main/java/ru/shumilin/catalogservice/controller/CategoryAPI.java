package ru.shumilin.catalogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.shumilin.catalogservice.dto.CategoryListResponseDto;
import ru.shumilin.catalogservice.dto.ErrorResponseDto;

import static ru.shumilin.catalogservice.util.ErrorTitleConstant.INTERNAL_SERVER_ERROR;

@Tag(name = "category-controller", description = "Взаимодействие с категориями")
public interface CategoryAPI {
    @Operation(summary = "Получение списка всех категорий товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список категорий успешно получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryListResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = INTERNAL_SERVER_ERROR,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    ResponseEntity<CategoryListResponseDto> findAll();
}
