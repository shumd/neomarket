package ru.shumilin.catalogservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.shumilin.catalogservice.dto.CategoryListResponseDto;
import ru.shumilin.catalogservice.service.CategoryService;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryRestController.class)
public class CategoryRestControllerTest {
    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void findAll_WithActiveStatusId_withValidServiceReturnData_return200HttpCode(){
        when(categoryService.findAllWithActiveStatusId())
                .thenReturn(new CategoryListResponseDto(new ArrayList<>()));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray());

        verify(categoryService, times(1)).findAllWithActiveStatusId();
    }

    @Test
    @SneakyThrows
    void findAll_WithActiveStatusId_whenAcceptHeaderNotContainsJson_return406HttpCode(){
        when(categoryService.findAllWithActiveStatusId())
                .thenReturn(new CategoryListResponseDto(new ArrayList<>()));

        mockMvc.perform(get("/categories").accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isNotAcceptable());
        verifyNoInteractions(categoryService);
    }

    @Test
    @SneakyThrows
    void findAll_WithActiveStatusId_whenServerError_return500HttpCode(){
        when(categoryService.findAllWithActiveStatusId())
                .thenThrow(new NullPointerException());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Unexpected error"));
    }
}
