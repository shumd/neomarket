package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.mapper.ItemMapper;
import ru.shumilin.catalogservice.repository.ItemRepository;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Value("${app.active-status-id}")
    private int activeStatusId;

    @Override
    public ItemResponseDto findById(int id) {
        return itemMapper.toResponseDto(itemRepository
                .findByIdAndStatusId(id, activeStatusId)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }
}
