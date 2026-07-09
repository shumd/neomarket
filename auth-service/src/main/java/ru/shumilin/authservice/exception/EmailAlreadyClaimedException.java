package ru.shumilin.authservice.exception;

public class EmailAlreadyClaimedException extends RuntimeException {
    public EmailAlreadyClaimedException(String email) {
        super("Email: %s already claimed".formatted(email));
    }
}
