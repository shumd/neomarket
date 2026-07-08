package ru.shumilin.authservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.shumilin.authservice.dto.response.ErrorResponseDto;

import static ru.shumilin.authservice.util.ErrorTitleConstant.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginAlreadyClaimedException.class)
    public ResponseEntity<ErrorResponseDto> handleLoginAlreadyClaimed(LoginAlreadyClaimedException e){
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(CONFLICT));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        log.warn(e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(BAD_REQUEST));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e){
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(new ErrorResponseDto(NOT_ACCEPTABLE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleBase(Exception e){
        log.warn(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto(INTERNAL_SERVICE_ERROR));
    }
}
