package ru.shumilin.authservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Ответ на аутентификацию пользователя")
public record LoginResponseDto(
        @Schema(description = "JWT-токен для авторизации", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikl2YW5vdiIsImlhdCI6MTUxNjIzOTAyMn0.XeXUo-1qrf3MqZ_zXl-HFJt5YK2BwJjCgG4Zz9hDgXQ")
        @NotNull
        String token //TODO написать комментарий что ТЗ пока не поправили
) {
}
