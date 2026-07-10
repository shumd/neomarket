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
import ru.shumilin.authservice.dto.request.LoginRequestDto;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.LogoutResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.exception.InvalidLoginDataException;
import ru.shumilin.authservice.model.LogoutStatus;
import ru.shumilin.authservice.service.JwtServiceTest;
import ru.shumilin.authservice.service.UsersService;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.shumilin.authservice.util.ErrorTitleConstant.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
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
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));
    }

    @Test
    @SneakyThrows
    public void login_withValidRequestBody_returnLoginResponseDto(){
        when(usersService.login(getLoginRequestDto(null, null)))
                .thenReturn(getLoginResponseDto());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test token"));
    }

    @Test
    @SneakyThrows
    public void login_withBlankEmail_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto("      ", null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_withTooShortEmail_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto("a".repeat(2), null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_withTooLongEmail_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto("a".repeat(51), null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_withInvalidEmailCharacters_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto("афвьв", null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_withBlankPassword_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, "     "))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_withTooShortPassword_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, "a".repeat(7)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_withTooLongPassword_return400HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, "a".repeat(73)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_whenWrongLoginOrPassword_return401HttpCode(){
        when(usersService.login(any())).thenThrow(InvalidLoginDataException.class);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, null))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED));
    }

    @Test
    @SneakyThrows
    public void login_withWrongAccept_return406HttpCode(){
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_PDF)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, null))))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value(NOT_ACCEPTABLE));
        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void login_whenInternalServerError_return500HttpCode(){
        when(usersService.login(any())).thenThrow(NullPointerException.class);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getLoginRequestDto(null, null))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));
    }

    @Test
    @SneakyThrows
    public void logout_withValidToken_returnLogoutResponseDto(){
        String token = new JwtServiceTest().getToken(null, null);

        when(usersService.logout())
                .thenReturn(new LogoutResponseDto("test", LogoutStatus.SUCCESS));

        mockMvc.perform(post("/auth/logout")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("test"))
                .andExpect(jsonPath("$.status").value(LogoutStatus.SUCCESS.value()));

        verify(usersService, times(1)).logout();
    }

    @Test
    @SneakyThrows
    public void logout_withInvalidToken_return401HttpCode(){
        String token = new JwtServiceTest()
                .getToken(new Date(System.currentTimeMillis()-1000), null);

        mockMvc.perform(post("/auth/logout")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED));

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void logout_withInvalidAccept_return406HttpCode(){
        String token = new JwtServiceTest()
                .getToken(null, null);

        mockMvc.perform(post("/auth/logout")
                        .accept(MediaType.APPLICATION_PDF)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotAcceptable());

        verifyNoInteractions(usersService);
    }

    @Test
    @SneakyThrows
    public void logout_whenInternalServerError_return500HttpCode(){
        String token = new JwtServiceTest()
                .getToken(null, null);

        when(usersService.logout()).thenThrow(NullPointerException.class);

        mockMvc.perform(post("/auth/logout")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));
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

    private LoginRequestDto getLoginRequestDto(String email, String password){
        return new LoginRequestDto(
                email == null ? "test_email@mail.ru" : email,
                password == null ? "test_password" : password
        );
    }

    private LoginResponseDto getLoginResponseDto(){
        return new LoginResponseDto("test token");
    }
}
