package ru.shumilin.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Ответ содержащий данные о регистрации пользователя")
public record RegisterResponseDto(

        @Schema(description = "Уникальный идентификатор пользователя",
                example = "0fbe4123-21f2-4b85-b4b1-ed6c23a08766")
        @NotNull
        UUID id,

        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NotBlank(message = "Last name cant be blank")
        @JsonProperty("last_name")
        String lastName,

        @Schema(description = "Имя пользователя", example = "Иван")
        @NotBlank(message = "First name cant be blank")
        @JsonProperty("first_name")
        String firstName,

        @Schema(description = "Почта", example = "ivan_ivanov@mail.ru")
        @NotBlank(message = "Email cant be blank")
        String email,

        @Schema(description = "Название роли", example = "Customer")
        @JsonProperty("role")
        @NotBlank(message = "Role name cant be blank")
        String roleName
) {
}
