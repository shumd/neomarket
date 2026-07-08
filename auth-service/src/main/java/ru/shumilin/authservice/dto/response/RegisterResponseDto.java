package ru.shumilin.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Ответ содержащий данные о регистрации пользователя")
public record RegisterResponseDto(

        @Schema(description = "Уникальный идентификатор пользователя",
                example = "0fbe4123-21f2-4b85-b4b1-ed6c23a08766")
        @NotNull
        UUID id,

        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NotNull
        @JsonProperty("last_name")
        String lastName,

        @Schema(description = "Имя пользователя", example = "Иван")
        @NotNull
        @JsonProperty("first_name")
        String firstName,

        @Schema(description = "Отчество пользователя", example = "Иванович")
        @JsonProperty("middle_name")
        String middleName,

        @Schema(description = "Логин", example = "ivan_ivanov")
        @NotNull
        String login,

        @Schema(description = "Название роли", example = "Customer")
        @JsonProperty("role")
        @NotNull
        String roleName
) {
}
