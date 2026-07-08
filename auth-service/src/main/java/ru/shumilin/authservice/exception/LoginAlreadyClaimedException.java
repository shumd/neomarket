package ru.shumilin.authservice.exception;

public class LoginAlreadyClaimedException extends RuntimeException {
    public LoginAlreadyClaimedException(String login) {
        super("Login: %s already claimed".formatted(login));
    }
}
