package ru.shumilin.catalogservice.controller;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.model.SortType;
import ru.shumilin.catalogservice.service.ItemService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductRestController implements ProductAPI {
    private final ItemService itemService;

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponseDto> findItemById(
            @PathVariable("id")
            @Min(value = 1, message = "Id must be positive")
            @Max(value = 2_147_483_647, message = "Id must not exceed 2147483647")
            Long id){
        return ResponseEntity.ok(itemService.findById(id.intValue()));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemPageResponseDto> findAllByName(
            @RequestParam(name = "category_id", required = false)
            @Positive(message = "Category id must be positive")
            Integer categoryId,

            @RequestParam(required = false)
            @Size(min = 1, max = 100, message = "Search length must be between 1 and 100")
            String search,

            @RequestParam(name = "sort", required = false)
            SortType sortType,

            @Min(value = 1, message = "Size must be between 1 and 100")
            @Max(value = 100, message = "Size must be between 1 and 100")
            @RequestParam(defaultValue = "20")
            int size,

            @PositiveOrZero(message = "Page must be positive or zero")
            @RequestParam(defaultValue = "0")
            int page){
            return ResponseEntity.ok(itemService.
                    findAllByName(categoryId, search, sortType, size, page));
    }
}