package ru.shumilin.catalogservice.service;

import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.model.SortType;

public interface ItemService {
    ItemResponseDto findById(int id);
    ItemPageResponseDto findAllByName(Integer categoryId,
                                      String search,
                                      SortType sortType,
                                      int limit,
                                      int offset);
}
