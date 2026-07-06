package ru.shumilin.catalogservice.service;

import ru.shumilin.catalogservice.dto.ItemResponseDto;

public interface ItemService {
    ItemResponseDto findById(int id);
}
