package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.mapper.ItemMapper;
import ru.shumilin.catalogservice.model.SortType;
import ru.shumilin.catalogservice.repository.ItemRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Value("${app.active-status-id}")
    private int activeStatusId;

    @Override
    public ItemResponseDto findById(int id) {
        log.info("Fetching product with id={}", id);
        return itemMapper.toResponseDto(itemRepository
                .findByIdAndStatusId(id, activeStatusId)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }

    @Override
    public ItemPageResponseDto findAllByName(Integer categoryId,
                                             String search,
                                             SortType sortType,
                                             int size,
                                             int page) {
        Sort sort;
        if (sortType == null) {
            sort = Sort.by("id").ascending();
        } else {
            sort = switch (sortType) {
                case NAME_ASC -> Sort.by("name").ascending();
                case NAME_DESC -> Sort.by("name").descending();
                case DATE_DESC -> Sort.by("date_registration").descending();
                case PRICE_ASC -> Sort.by("price").ascending();
                case PRICE_DESC -> Sort.by("price").descending();
            };
        }
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        log.info("Fetching products with categoryId={}, " +
                "search={}, sortType={}, size={}, page={}",
                categoryId, search, sortType == null ? "ID_ASC" : sortType, size, page);
        return itemMapper.toPageResponseDto(
                itemRepository.findAllByName(search, categoryId, pageRequest)
                .map(itemMapper::toWithSupplierResponseDto));
    }
}
