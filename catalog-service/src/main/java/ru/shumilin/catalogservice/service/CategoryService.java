package ru.shumilin.catalogservice.service;


import ru.shumilin.catalogservice.dto.CategoryListResponseDto;

public interface CategoryService {
    CategoryListResponseDto findAllWithActiveStatusId();
}
