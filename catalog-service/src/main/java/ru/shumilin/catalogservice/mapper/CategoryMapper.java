package ru.shumilin.catalogservice.mapper;

import org.springframework.stereotype.Component;
import ru.shumilin.catalogservice.dto.CategoryResponseDto;
import ru.shumilin.catalogservice.model.entity.CategoryEntity;

@Component
public class CategoryMapper {
    public CategoryResponseDto toResponseDto(CategoryEntity entity){
        return new CategoryResponseDto(
                entity.getId().toString(),
                entity.getName()
        );
    }
}
