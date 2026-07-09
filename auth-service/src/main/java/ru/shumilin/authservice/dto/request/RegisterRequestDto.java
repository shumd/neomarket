package ru.shumilin.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию пользователя")
public record RegisterRequestDto(

        @Schema(description = "Фамилия пользователя", example = "Иванов",
                minLength = 1, maxLength = 200)
        @JsonProperty("last_name")
        @NotNull
        @Size(min = 1, max = 200, message = "Size must be between 1 and 200")
        String lastName,

        @Schema(description = "Имя пользователя", example = "Иван",
                minLength = 1, maxLength = 200)
        @JsonProperty("first_name")
        @NotNull
        @Size(min = 1, max = 200, message = "Size must be between 1 and 200")
        String firstName,

        @Schema(description = "Почта", example = "ivan_ivanov@mail.ru",
                minLength = 3, maxLength = 50, pattern = "^[a-zA-Z0-9_@.]+$")
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50")
        @NotBlank(message = "Email cant be blank")
        @Pattern(
                regexp = "^[a-zA-Z0-9_@.]+$",
                message = "Email can contain only letters, numbers, @, dot and underscore"
        )
        String email,

        @Schema(description = "Пароль", example = "password123",
                minLength = 8, maxLength = 72)
        @NotBlank(message = "Password cant be blank")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 symbols")
        String password
) {
}
