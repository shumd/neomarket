package ru.shumilin.authservice.exception;

public class TokenInBlackListException extends RuntimeException {
    public TokenInBlackListException() {
        super("Token is in the black list");
    }
}
