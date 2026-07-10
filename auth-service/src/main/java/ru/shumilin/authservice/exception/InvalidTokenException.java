package ru.shumilin.authservice.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: %s".formatted(token));
    }
}
