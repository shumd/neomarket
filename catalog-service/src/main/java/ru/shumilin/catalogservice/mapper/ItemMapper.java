package ru.shumilin.catalogservice.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.dto.ItemWithSupplierResponseDto;
import ru.shumilin.catalogservice.model.entity.ItemEntity;
import ru.shumilin.catalogservice.model.projection.ItemWithSupplierProjection;

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
                entity.getQuantity()
        );
    }

    public ItemWithSupplierResponseDto toWithSupplierResponseDto(ItemWithSupplierProjection itemWithSupplierProjection) {
        return new ItemWithSupplierResponseDto(
                itemWithSupplierProjection.getId().toString(),
                itemWithSupplierProjection.getName(),
                itemWithSupplierProjection.getPrice(),
                itemWithSupplierProjection.getSupplierName(),
                itemWithSupplierProjection.getQuantity()
        );
    }

    public ItemPageResponseDto toPageResponseDto(Page<ItemWithSupplierResponseDto> itemWithSupplierResponseDtoPage){
        return new ItemPageResponseDto(
                itemWithSupplierResponseDtoPage.getContent(),
                itemWithSupplierResponseDtoPage.getSize(),
                itemWithSupplierResponseDtoPage.getPageable().getPageNumber()
        );
    }
}
