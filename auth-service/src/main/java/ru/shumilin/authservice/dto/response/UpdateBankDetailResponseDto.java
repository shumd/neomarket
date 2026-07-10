package ru.shumilin.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Ответ содержащий актуальные банковские реквизиты пользователя")
public record UpdateBankDetailResponseDto(
        @Schema(description = "Банковские реквизиты пользователя", example = "402466464461535134847879")
        @JsonProperty("bank_detail")
        @NotBlank
        String bankDetail
) {
}
