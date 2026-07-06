package ru.shumilin.catalogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.shumilin.catalogservice.dto.ErrorResponseDto;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.model.SortType;

import static ru.shumilin.catalogservice.util.ErrorTitleConstant.INTERNAL_SERVER_ERROR;

@Tag(name = "product-controller", description = "Взаимодействие с товарами")
public interface ProductAPI {
    @Operation(summary = "Получение товара по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар успешно получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Товар с указанным id не найден в системе",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = INTERNAL_SERVER_ERROR,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    ResponseEntity<ItemResponseDto> findItemById(
            @Parameter(
                    description = "Идентификатор товара",
                    example = "1",
                    required = true
            )
            Long id);

    @Operation(summary = "Получение списка товаров с фильтрацией и сортировкой")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары успешно получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemPageResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = INTERNAL_SERVER_ERROR,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    ResponseEntity<ItemPageResponseDto> findAllByName(
            @Parameter(
                    description = "Фильтр по id категории",
                    example = "1"
            )
            Integer categoryId,

            @Parameter(
                    description = "Поиск по названию товара",
                    example = "смартфон"
            )
            String search,

            @Parameter(
                    description = "Поле и направление сортировки",
                    example = "price_asc"
            )
            SortType sortType,

            @Parameter(
                    description = "Количество товаров на странице",
                    example = "20"
            )
            int size,

            @Parameter(
                    description = "Смещение для пагинации",
                    example = "0"
            )
            int page);
}
