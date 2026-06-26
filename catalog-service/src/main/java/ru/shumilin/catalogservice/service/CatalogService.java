package ru.shumilin.catalogservice.service;

import ru.shumilin.catalogservice.dto.ItemResponseDto;

public interface CatalogService {
    ItemResponseDto findById(int id);
}
