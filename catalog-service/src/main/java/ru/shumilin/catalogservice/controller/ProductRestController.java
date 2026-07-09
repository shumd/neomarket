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
            Long id){
        return ResponseEntity.ok(itemService.findById(id.intValue()));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemPageResponseDto> findAllByName(
            @RequestParam(name = "category_id", required = false)
            Integer categoryId,

            @RequestParam(required = false)
            String search,

            @RequestParam(name = "sort", required = false)
            SortType sortType,

            @RequestParam(defaultValue = "20")
            int size,

            @RequestParam(defaultValue = "0")
            int page){
            return ResponseEntity.ok(itemService.
                    findAllByName(categoryId, search, sortType, size, page));
    }
}