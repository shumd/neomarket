package ru.shumilin.catalogservice.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.service.ItemService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductRestController {
    private final ItemService itemService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponseDto> findItemById(
            @PathVariable("id")
            @Min(value = 1, message = "Id must be positive")
            @Max(value = 2_147_483_647, message = "Id must not exceed 2147483647")
            Long id){
        return ResponseEntity.ok(itemService.findById(id.intValue()));
    }
}