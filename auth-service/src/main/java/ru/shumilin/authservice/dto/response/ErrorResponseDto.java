package ru.shumilin.authservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import static ru.shumilin.authservice.util.ErrorTitleConstant.BAD_REQUEST;

@Schema(description = "Ответ содержащий ошибку")
public record ErrorResponseDto(
        @Schema(description = "Сообщение об ошибке", example = BAD_REQUEST)
        String message
) {
}
