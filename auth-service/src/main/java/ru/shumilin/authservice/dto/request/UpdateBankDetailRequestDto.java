package ru.shumilin.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на добавление/обновление банковских реквизитов пользователя")
public record UpdateBankDetailRequestDto(
        @Schema(description = "Банковские реквизиты пользователя", example = "402466464461535134847879")
        @JsonProperty("bank_detail")
        @NotBlank
        String bankDetail
) {
}
