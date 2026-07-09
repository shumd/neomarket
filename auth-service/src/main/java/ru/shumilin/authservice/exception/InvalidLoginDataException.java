package ru.shumilin.authservice.exception;

public class InvalidLoginDataException extends RuntimeException {
    public InvalidLoginDataException(String login) {
        super("Invalid login data for user with login: %s".formatted(login));
    }
}
