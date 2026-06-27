package ru.shumilin.catalogservice.controller;

import jakarta.validation.constraints.Positive;
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
@RequestMapping("/products") // api/v1 ?
@RequiredArgsConstructor
@Validated
public class ProductRestController {
    private final ItemService itemService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponseDto> findItemById(
            @PathVariable
            @Positive(message = "Id must be positive")
            int id){
        return ResponseEntity.ok(itemService.findById(id));
    }
}