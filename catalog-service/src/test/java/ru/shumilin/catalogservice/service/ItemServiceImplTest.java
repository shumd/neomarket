package ru.shumilin.catalogservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.entity.ItemEntity;
import ru.shumilin.catalogservice.exception.ItemNotActiveException;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.mapper.ItemMapper;
import ru.shumilin.catalogservice.repository.ItemRepository;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void findById_withValidId_returnItemResponseDto(){
        ReflectionTestUtils.setField(itemService, "activeStatusId", 1);

        ItemEntity itemEntity = ItemEntity.builder()
                .id(10)
                .name("Test item")
                .description("Test description")
                .price(new BigDecimal("1234.56"))
                .categoryId(1)
                .statusId(1)
                .orgSupplierId(1)
                .quantity(1)
                .build();

        ItemResponseDto itemResponseDto = new ItemResponseDto(
                "10",
                "Test item",
                "Test description",
                new BigDecimal("1234.56"),
                "1",
                "1",
                1);

        Mockito.when(itemRepository.findById(10))
                .thenReturn(Optional.of(itemEntity));

        Mockito.when(itemMapper.toResponseDto(itemEntity))
                .thenReturn(itemResponseDto);

        Assertions.assertEquals(itemResponseDto, itemService.findById(10));
    }

    @Test
    void findById_withInvalidId_throwItemNotFoundException(){
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.findById(1));
    }

    @Test
    void findById_withInvalidStatusId_throwItemNotActiveException(){
        ReflectionTestUtils.setField(itemService, "activeStatusId", 1);

        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(
                ItemEntity.builder()
                        .id(1)
                        .name("Test name")
                        .price(new BigDecimal("1234.56"))
                        .quantity(1)
                        .statusId(2)
                        .categoryId(2)
                        .build()
        ));

        Assertions.assertThrows(ItemNotActiveException.class,
                () -> itemService.findById(1));
    }
}