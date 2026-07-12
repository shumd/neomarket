package ru.shumilin.catalogservice.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shumilin.catalogservice.dto.CategoryResponseDto;
import ru.shumilin.catalogservice.model.entity.CategoryEntity;

public class CategoryMapperTest {
    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    void toResponseDto_WithValidParam_returnCategoryResponseDto(){
        CategoryResponseDto excepted = new CategoryResponseDto("1", "test");

        CategoryEntity entity = CategoryEntity.builder()
                .id(1)
                .name("test")
                .statusId(1)
                .comment("comment")
                .build();

        Assertions.assertEquals(excepted, categoryMapper.toResponseDto(entity));
    }
}
