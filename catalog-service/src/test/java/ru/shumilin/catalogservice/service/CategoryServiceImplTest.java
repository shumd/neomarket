package ru.shumilin.catalogservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shumilin.catalogservice.dto.CategoryListResponseDto;
import ru.shumilin.catalogservice.dto.CategoryResponseDto;
import ru.shumilin.catalogservice.mapper.CategoryMapper;
import ru.shumilin.catalogservice.model.entity.CategoryEntity;
import ru.shumilin.catalogservice.repository.CategoryRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_whenRepositoryReturnEntitiesWithValidCategoryId_returnSortedResponseDtoList() {
        ReflectionTestUtils.setField(categoryService, "activeStatusId", 1);

        CategoryEntity firstEntity = CategoryEntity.builder()
                .id(1).name("B").statusId(1).comment("comm").build();
        CategoryEntity secondEntity = CategoryEntity.builder()
                .id(2).name("A").statusId(1).comment("comm").build();

        CategoryResponseDto firstResponseDto = new CategoryResponseDto("1", "B");
        CategoryResponseDto secondResponseDto = new CategoryResponseDto("2", "A");

        when(categoryMapper.toResponseDto(firstEntity)).thenReturn(firstResponseDto);
        when(categoryMapper.toResponseDto(secondEntity)).thenReturn(secondResponseDto);

        when(categoryRepository.findAll()).thenReturn(List.of(firstEntity, secondEntity));

        Assertions.assertEquals(new CategoryListResponseDto(List.of(secondResponseDto, firstResponseDto)),
                categoryService.findAll());
    }

    @Test
    void findAll_whenRepositoryReturnEmptyList_returnEmptyResponseDtoList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        Assertions.assertEquals(new CategoryListResponseDto(List.of()), categoryService.findAll());
    }
}
