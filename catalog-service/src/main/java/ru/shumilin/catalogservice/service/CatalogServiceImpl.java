package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.mapper.ItemMapper;
import ru.shumilin.catalogservice.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponseDto findById(int id) {
        return itemMapper.toResponseDto(itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }
}
