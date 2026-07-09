package ru.shumilin.authservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Ответ на аутентификацию пользователя")
public record LoginResponseDto(

        @Schema(description = "JWT-токен для авторизации", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikl2YW5vdiIsImlhdCI6MTUxNjIzOTAyMn0.XeXUo-1qrf3MqZ_zXl-HFJt5YK2BwJjCgG4Zz9hDgXQ")
        @NotNull
        String token,

        @Schema(description = "Уникальный идентификатор пользователя", example = "0fbe4123-21f2-4b85-b4b1-ed6c23a08766")
        @NotNull
        UUID id,

        @Schema(description = "Логин пользователя", example = "ivanov_123")
        @NotNull
        String login,

        @Schema(description = "Название роли пользователя", example = "Покупатель")
        @NotNull
        String role
) {
}
