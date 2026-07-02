package ru.shumilin.catalogservice.mapper;

import org.springframework.stereotype.Component;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.entity.ItemEntity;

@Component
public class ItemMapper {

    public ItemResponseDto toResponseDto(ItemEntity entity){
        return new ItemResponseDto(
                entity.getId().toString(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategoryId() == null ? null : entity.getCategoryId().toString(),
                entity.getOrgSupplierId() == null ? null : entity.getOrgSupplierId().toString(),
                entity.getQuantity() //TODO Quantity должен синхронизироваться с actualisation-service
        );
    }
}
