package ru.shumilin.catalogservice.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.dto.ItemWithSupplierResponseDto;
import ru.shumilin.catalogservice.model.entity.ItemEntity;
import ru.shumilin.catalogservice.model.projection.ItemWithSupplierProjection;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void toWithSupplierResponseDto_withValidProjection_returnWithSupplierResponseDto(){
        ItemWithSupplierProjection projection = mock(ItemWithSupplierProjection.class);
        when(projection.getId()).thenReturn(10);
        when(projection.getName()).thenReturn("Test item");
        when(projection.getSupplierName()).thenReturn("Test supplier");
        when(projection.getPrice()).thenReturn(new BigDecimal("1234.56"));
        when(projection.getQuantity()).thenReturn(1);

        ItemWithSupplierResponseDto withSupplierResponseDto = new ItemWithSupplierResponseDto(
                "10",
                "Test item",
                new BigDecimal("1234.56"),
                "Test supplier",
                1
        );

        Assertions.assertEquals(withSupplierResponseDto, itemMapper.toWithSupplierResponseDto(projection));
    }

    @Test
    void toPageResponseDto_withValidPage_returnPageResponseDto(){
        List<ItemWithSupplierResponseDto> content = List.of(
                new ItemWithSupplierResponseDto("1","test1", new BigDecimal("123.45"), "sup1",1),
                new ItemWithSupplierResponseDto("2","test2", new BigDecimal("143.55"), "sup2",3)
        );

        Page<ItemWithSupplierResponseDto> page = new PageImpl<>(
                content,
                PageRequest.of(0,5),
                content.size());

        ItemPageResponseDto itemPageResponseDto = new ItemPageResponseDto(
                content,
                page.getSize(),
                page.getPageable().getOffset()
        );

        Assertions.assertEquals(itemPageResponseDto, itemMapper.toPageResponseDto(page));
    }
}
