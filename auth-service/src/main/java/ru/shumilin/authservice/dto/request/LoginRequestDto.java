package ru.shumilin.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на аутентификацию пользователя")
public record LoginRequestDto(

        @Schema(description = "Почта", example = "ivan_ivanov@mail.ru",
                minLength = 3, maxLength = 50, pattern = "^[a-zA-Z0-9_@.]+$")
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50")
        @NotBlank(message = "Email cant be blank")
        @Pattern(
                regexp = "^[a-zA-Z0-9_@.]+$",
                message = "Email can contain only letters, numbers, @, dot and underscore"
        )
        String email,

        @Schema(description = "Пароль пользователя", example = "password123")
        @NotEmpty(message = "Password can`t be empty")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 symbols")
        String password
) {
}
