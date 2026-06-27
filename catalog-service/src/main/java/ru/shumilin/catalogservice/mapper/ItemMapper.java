package ru.shumilin.catalogservice.mapper;

import org.springframework.stereotype.Component;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.entity.ItemEntity;

@Component
public class ItemMapper { //Использовать mapStruct ?

    public ItemResponseDto toResponseDto(ItemEntity entity){
        return new ItemResponseDto(
                entity.getId().toString(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategoryId().toString(),
                entity.getOrgSupplierId().toString(),
                0 // quantity должно получаться из сервиса склада
        );
    }
}
