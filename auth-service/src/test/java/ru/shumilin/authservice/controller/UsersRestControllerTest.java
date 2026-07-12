package ru.shumilin.authservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.shumilin.authservice.dto.request.UpdateBankDetailRequestDto;
import ru.shumilin.authservice.dto.response.UpdateBankDetailResponseDto;
import ru.shumilin.authservice.service.JwtServiceTest;
import ru.shumilin.authservice.service.UsersService;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.shumilin.authservice.util.ErrorTitleConstant.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class UsersRestControllerTest {
    @MockitoBean
    private UsersService usersService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void updateBankDetail_withValidTokenAndRequestDto_returnUpdateBankDetailDto() {
        String token = new JwtServiceTest().getToken(null, null);

        when(usersService.updateBankDetail(anyString(), any()))
                .thenReturn(new UpdateBankDetailResponseDto("test"));

        mockMvc.perform(put("/users/me/bank-details")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBankDetailRequestDto("test")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bank_detail").value("test"));

        verify(usersService, times(1)).updateBankDetail(anyString(), any());
    }

    @Test
    @SneakyThrows
    public void updateBankDetail_withNullRequestDto_return400() {
        String token = new JwtServiceTest().getToken(null, null);

        mockMvc.perform(put("/users/me/bank-details")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBankDetailRequestDto(null)))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void updateBankDetail_withBlankRequestDto_return400() {
        String token = new JwtServiceTest().getToken(null, null);

        mockMvc.perform(put("/users/me/bank-details")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBankDetailRequestDto("     ")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void updateBankDetail_withInvalidToken_return401() {
        String token = new JwtServiceTest().getToken(new Date(System.currentTimeMillis() - 1000),
                null);

        mockMvc.perform(put("/users/me/bank-details")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBankDetailRequestDto("test")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void updateBankDetail_withInvalidAcceptHeader_return406() {
        String token = new JwtServiceTest().getToken(null, null);

        mockMvc.perform(put("/users/me/bank-details")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_PDF)
                        .content(objectMapper.writeValueAsString(new UpdateBankDetailRequestDto("test")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value(NOT_ACCEPTABLE));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void updateBankDetail_whenInternalServerError_return500() {
        String token = new JwtServiceTest().getToken(null, null);

        when(usersService.updateBankDetail(anyString(), any()))
                .thenThrow(NullPointerException.class);

        mockMvc.perform(put("/users/me/bank-details")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateBankDetailRequestDto("test")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));
    }
}