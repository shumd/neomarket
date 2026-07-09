package ru.shumilin.authservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.config.SecurityConfig;
import ru.shumilin.authservice.service.UsersService;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.shumilin.authservice.util.ErrorTitleConstant.*;

@WebMvcTest(AuthRestController.class)
@Import(SecurityConfig.class)
public class AuthRestControllerTest {
    @MockitoBean
    private UsersService usersService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void register_withValidRequestBody_returnRegisterResponseDto(){
        when(usersService.register(any())).thenReturn(getRegisterResponseDto());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, null, null))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test_email"));
    }

    @Test
    @SneakyThrows
    public void register_withEmptyLastName_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto("", null, null, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withTooLongLastName_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto("a".repeat(201), null, null, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withEmptyFirstName_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, "", null, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withTooLongFirstName_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, "a".repeat(201), null, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withEmailLessThan3_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, "a".repeat(2), null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withTooLongEmail_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, "a".repeat(51), null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withInvalidEmailCharacters_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, "Абгдфы", null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withPasswordLessThan8_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, null, "a".repeat(7)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withTooLongPassword_return400HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, null, "a".repeat(73)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_withWrongAccept_return406HttpCode(){
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_PDF)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, null, null))))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value(NOT_ACCEPTABLE));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void register_whenServerError_return500HttpCode(){
        when(usersService.register(any())).thenThrow(NullPointerException.class);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRegisterRequestDto(null, null, null, null))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVICE_ERROR));
    }

    private RegisterResponseDto getRegisterResponseDto(){
        return new RegisterResponseDto(
                UUID.randomUUID(),
                "test lastName",
                "test firstName",
                "test_email",
                "test roleName"
        );
    }

    private RegisterRequestDto getRegisterRequestDto(String lastName,
                                                     String firstName,
                                                     String email,
                                                     String password){
        return new RegisterRequestDto(
                lastName == null ? "test lastName" : lastName,
                firstName == null ? "test firstName" : firstName,
                email == null ? "test_email" : email,
                password == null ? "12345678" : password);
    }
}
