package ru.shumilin.catalogservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.shumilin.catalogservice.dto.ItemPageResponseDto;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.dto.ItemWithSupplierResponseDto;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.model.SortType;
import ru.shumilin.catalogservice.service.ItemService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {
    @MockitoBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void findItemById_withValidId_returnItem(){
        ItemResponseDto itemResponseDto = new ItemResponseDto(
                "1",
                "Test item",
                "Test description",
                new BigDecimal("1234.56"),
                "1",
                "1",
                1);

        when(itemService.findById(1)).thenReturn(itemResponseDto);

        mockMvc.perform(get("/products/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test item"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.price").value(1234.56))
                .andExpect(jsonPath("$.id_category").value("1"))
                .andExpect(jsonPath("$.id_org_supplier").value("1"))
                .andExpect(jsonPath("$.quantity").value(1));
        verify(itemService, times(1)).findById(1);
    }

    @Test
    @SneakyThrows
    void findItemById_withInvalidId_return400HttpCode(){
        mockMvc.perform(get("/products/{id}",-1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Id must be positive"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findItemById_withTooLongId_return400HttpCode(){
        mockMvc.perform(get("/products/{id}", 129575218768L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Id must not exceed 2147483647"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findItemById_withEmptyId_return404HttpCode(){
        mockMvc.perform(get("/products/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Resource not found"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findItemById_withNonExistingId_return404HttpCode(){
        int id = 2532;

        when(itemService.findById(id)).thenThrow(new ItemNotFoundException(id));

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Item with id %d not found".formatted(id)));
    }

    @Test
    @SneakyThrows
    void findItemById_withInvalidAcceptHeader_return406HttpCode(){
        mockMvc.perform(get("/products/{id}", 1)
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isNotAcceptable());
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findItemById_whenServerError_return500HttpCode(){
        when(itemService.findById(1))
                .thenThrow(NullPointerException.class);

        mockMvc.perform(get("/products/{id}",1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Unexpected error"));
    }

    @Test
    @SneakyThrows
    void findAllByName_withoutParam_returnFirstPageSortedById() {
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(getItemPageResponseDto());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].id").value("5"));

        verify(itemService, times(1))
                .findAllByName(isNull(), isNull(), eq(SortType.ID_ASC), eq(20), eq(0));
    }

    @Test
    @SneakyThrows
    void findAllByName_withValidSearchParam_returnFirstPageContainsSearch() {
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(getItemPageResponseDto());

        mockMvc.perform(get("/products?search=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].name").value("item 5"));

        verify(itemService, times(1))
                .findAllByName(isNull(), eq("5"), eq(SortType.ID_ASC), eq(20), eq(0));
    }

    @Test
    @SneakyThrows
    void findAllByName_withEmptySearchParam_return400HttpCode() {
        mockMvc.perform(get("/products?search="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Search length must be between 1 and 100"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withTooLongSearchParam_returnFirstPageContainsSearch() {
        mockMvc.perform(get("/products?search=%s".formatted("a".repeat(101))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Search length must be between 1 and 100"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withValidCategoryIdParam_returnFirstPageContainsCategoryId(){
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(getItemPageResponseDto());

        mockMvc.perform(get("/products?category_id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isNotEmpty());

        verify(itemService, times(1))
                .findAllByName(eq(1), any(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void findAllByName_withNegativeCategoryIdParam_return400HttpCode(){
        mockMvc.perform(get("/products?category_id=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Category id must be positive"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withValidSortParam_returnFirstSortedPage(){
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(getItemPageResponseDto());

        mockMvc.perform(get("/products?sort=name_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isNotEmpty());

        verify(itemService, times(1))
                .findAllByName(any(),any(), eq(SortType.NAME_DESC), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void findAllByName_withInvalidSortParam_return400HttpCode(){
        mockMvc.perform(get("/products?sort=123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Invalid request parameter"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withValidSizeParam_returnFirstPageWithGiveSize(){
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new ItemPageResponseDto(
                        List.of(getItemWithSupplierResponseDto()),
                        1,
                        0
                ));

        mockMvc.perform(get("/products?size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.size").value(1));

        verify(itemService, times(1))
                .findAllByName(any(), any(), any(), eq(1), anyInt());
    }

    @Test
    @SneakyThrows
    void findAllByName_withNegativeSizeParam_return400HttpCode(){
        mockMvc.perform(get("/products?size=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Size must be between 1 and 100"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withTooLargeSizeParam_return400HttpCode(){
        mockMvc.perform(get("/products?size=101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Size must be between 1 and 100"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withValidPageParam_returnGivenPage(){
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new ItemPageResponseDto(
                        List.of(getItemWithSupplierResponseDto()),
                        1,
                        3
                ));

        mockMvc.perform(get("/products?page=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.page").value(3));

        verify(itemService, times(1))
                .findAllByName(any(), any(), any(), anyInt(), eq(3));
    }

    @Test
    @SneakyThrows
    void findAllByName_withNegativePageParam_return400HttpCode(){
        mockMvc.perform(get("/products?page=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Page must be positive or zero"));

        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_withInvalidAcceptHeader_return406HttpCode() {
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isNotAcceptable());
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_when_return406HttpCode() {
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isNotAcceptable());
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void findAllByName_whenServerError_return500HttpCode(){
        when(itemService.findAllByName(any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Unexpected error"));
    }

    private ItemWithSupplierResponseDto getItemWithSupplierResponseDto(){
        return new ItemWithSupplierResponseDto(
                "5",
                "item 5",
                new BigDecimal("1234.56"),
                "supName",
                1
        );
    }

    private ItemPageResponseDto getItemPageResponseDto(){
        return new ItemPageResponseDto(
                List.of(getItemWithSupplierResponseDto()),
                20,
                0);
    }
}