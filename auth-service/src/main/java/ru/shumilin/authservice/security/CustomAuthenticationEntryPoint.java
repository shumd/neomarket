package ru.shumilin.authservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.shumilin.authservice.dto.response.ErrorResponseDto;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static ru.shumilin.authservice.util.ErrorTitleConstant.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void commence(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AuthenticationException authException){

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        objectMapper.writeValue(
                response.getWriter(),
                new ErrorResponseDto(UNAUTHORIZED)
        );
    }
}
