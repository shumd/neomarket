package ru.shumilin.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterResponseDto(

        @NotNull
        UUID id,

        @NotNull
        @JsonProperty("last_name")
        String lastName,

        @NotNull
        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("middle_name")
        String middleName,

        String login,

        @JsonProperty("role")
        String roleName
) {
}
