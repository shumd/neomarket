package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.CategoryListResponseDto;
import ru.shumilin.catalogservice.mapper.CategoryMapper;
import ru.shumilin.catalogservice.repository.CategoryRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Value("${app.active-status-id}")
    private int activeStatusId;

    @Override
    public CategoryListResponseDto findAllWithActiveStatusId() {
        log.info("Fetching categories with activeStatusId={}", activeStatusId);
        CategoryListResponseDto res = new CategoryListResponseDto(
                categoryRepository.findAllByStatusIdOrderByNameAsc(activeStatusId)
                        .stream()
                        .map(categoryMapper::toResponseDto)
                        .toList());

        if(res.categories().isEmpty()){
            log.warn("No active categories were found (activeStatusId={})", activeStatusId);
        }

        return res;
    }
}
