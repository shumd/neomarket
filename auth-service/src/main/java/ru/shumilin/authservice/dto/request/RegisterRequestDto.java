package ru.shumilin.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @JsonProperty("last_name")
        @NotNull
        String lastName,

        @JsonProperty("first_name")
        @NotNull
        String firstName,

        @JsonProperty("middle_name")
        String middleName,

        @Size(min = 3, max = 50, message = "Login must be between 3 and 50")
        String login,

        @Size(min = 8, message = "Password must be longer than 8 symbols")
        String password
) {
}
