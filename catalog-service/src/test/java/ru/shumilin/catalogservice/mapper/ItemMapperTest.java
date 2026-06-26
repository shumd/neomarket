package ru.shumilin.catalogservice.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.entity.ItemEntity;

import java.math.BigDecimal;

public class ItemMapperTest {
    private final ItemMapper itemMapper = new ItemMapper();

    @Test
    void toResponseDto_withValidEntity_returnResponseDto(){
        ItemEntity itemEntity = ItemEntity.builder()
                .id(10)
                .name("Test item")
                .description("Test description")
                .price(new BigDecimal("1234.56"))
                .categoryId(1)
                .orgSupplierId(1)
                .quantity(1)
                .build();

        ItemResponseDto exceptedDto = new ItemResponseDto(
                "10",
                "Test item",
                "Test description",
                new BigDecimal("1234.56"),
                "1",
                "1",
                1);

        Assertions.assertEquals(exceptedDto, itemMapper.toResponseDto(itemEntity));
    }
}
