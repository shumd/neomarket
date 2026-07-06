package ru.shumilin.catalogservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.shumilin.catalogservice.dto.ItemResponseDto;
import ru.shumilin.catalogservice.exception.ItemNotFoundException;
import ru.shumilin.catalogservice.service.ItemService;

import java.math.BigDecimal;

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
}