package ru.shumilin.authservice.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LogoutStatus {
    SUCCESS;

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }
}
