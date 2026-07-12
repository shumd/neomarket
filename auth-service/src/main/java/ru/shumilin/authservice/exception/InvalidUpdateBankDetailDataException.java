package ru.shumilin.authservice.exception;

public class InvalidUpdateBankDetailDataException extends RuntimeException {
    public InvalidUpdateBankDetailDataException(String message) {
        super(message);
    }
}
