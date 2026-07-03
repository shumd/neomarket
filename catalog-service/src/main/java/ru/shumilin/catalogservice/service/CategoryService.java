package ru.shumilin.catalogservice.service;

import ru.shumilin.catalogservice.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();
}
