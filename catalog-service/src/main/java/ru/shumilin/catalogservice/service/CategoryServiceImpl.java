package ru.shumilin.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shumilin.catalogservice.dto.CategoryListResponseDto;
import ru.shumilin.catalogservice.dto.CategoryResponseDto;
import ru.shumilin.catalogservice.mapper.CategoryMapper;
import ru.shumilin.catalogservice.repository.CategoryRepository;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Value("${app.active-status-id}")
    private int activeStatusId;

    @Override
    public CategoryListResponseDto findAll() {
        log.info("Fetching categories with activeStatusId={}", activeStatusId);
        CategoryListResponseDto res = new CategoryListResponseDto(
                StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .filter(entity -> Objects.equals(entity.getStatusId(), activeStatusId))
                .map(categoryMapper::toResponseDto)
                .sorted(Comparator.comparing(CategoryResponseDto::name))
                .toList());

        if(res.categories().isEmpty()){
            log.warn("No active categories was found (activeStatusId={}", activeStatusId);
        }

        return res;
    }
}
