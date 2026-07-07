package ru.shumilin.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @JsonProperty("last_name")
        @NotNull
        @Size(min = 1, max = 200, message = "Size must be between 1 and 200")
        String lastName,

        @JsonProperty("first_name")
        @NotNull
        @Size(min = 1, max = 200, message = "Size must be between 1 and 200")
        String firstName,

        @JsonProperty("middle_name")
        @Size(max = 200, message = "Size must be less than 200")
        String middleName,

        @Size(min = 3, max = 50, message = "Login must be between 3 and 50")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "Login can contain only letters, numbers and underscore"
        )
        String login,

        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 symbols")
        String password
) {
}
