package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.entity.ItemEntity;
import ru.shumilin.catalogservice.exception.ItemNotActiveException;
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
        ItemEntity entity = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if(entity.getStatusId() == null || entity.getStatusId() != activeStatusId){
            throw new ItemNotActiveException(id);
        }

        return itemMapper.toResponseDto(entity);
    }
}
