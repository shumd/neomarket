package ru.shumilin.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shumilin.catalogservice.dto.CategoryListResponseDto;
import ru.shumilin.catalogservice.service.CategoryService;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestController implements CategoryAPI{
    private final CategoryService categoryService;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryListResponseDto> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
