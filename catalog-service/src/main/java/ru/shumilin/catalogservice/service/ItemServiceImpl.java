package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.mapper.ItemMapper;
import ru.shumilin.catalogservice.model.SortType;
import ru.shumilin.catalogservice.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponseDto findById(int id) {
        return itemMapper.toResponseDto(itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }

    @Override
    public ItemPageResponseDto findAllByName(Integer categoryId,
                                             String search,
                                             SortType sortType,
                                             int limit,
                                             int offset) {
        Sort sort = switch (sortType){
            case ID_ASC -> Sort.by("id").ascending();
            case NAME_ASC -> Sort.by("name").ascending();
            case NAME_DESC -> Sort.by("name").descending();
            case DATE_DESC -> Sort.by("date_registration").descending();
            case PRICE_ASC -> Sort.by("price").ascending();
            case PRICE_DESC -> Sort.by("price").descending();
        };
        PageRequest pageRequest = PageRequest.of(offset/limit, limit, sort);
        return itemMapper.toPageResponseDto(
                itemRepository.findAllByName(search, categoryId, pageRequest)
                .map(itemMapper::toWithSupplierResponseDto));
    }
}
