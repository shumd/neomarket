package ru.shumilin.authservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.shumilin.authservice.model.LogoutStatus;

@Schema(description = "Ответ содержащий данные о выходе пользователя из системы")
public record LogoutResponseDto(

        @Schema(description = "Сообщение об успешном выходе", example = "Выход выполнен успешно")
        @NotBlank
        String message,

        @Schema(description = "Статус операции", example = "success")
        @NotNull
        LogoutStatus status
) {
}
