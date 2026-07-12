package ru.tataev.basket_service.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tataev.basket_service.dto.ErrorResponseDto;
import ru.tataev.basket_service.exception.BusinessLogicException;
import ru.tataev.basket_service.exception.InvalidRequestException;
import ru.tataev.basket_service.exception.ResourceNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getField() + ": " + x.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErrorResponseDto res = new ErrorResponseDto(400, "Bad Request", message);
        return ResponseEntity.status(400).body(res);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleNotReadable(HttpMessageNotReadableException ex){
        ErrorResponseDto res = new ErrorResponseDto(400, "Bad Request", "Некорректный формат запроса");
        return ResponseEntity.status(400).body(res);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequest(InvalidRequestException ex) {
        ErrorResponseDto res = new ErrorResponseDto(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(400).body(res);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessLogic(BusinessLogicException ex) {
        ErrorResponseDto res = new ErrorResponseDto(422, "Unprocessable Entity", ex.getMessage());
        return ResponseEntity.status(422).body(res);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDto res = new ErrorResponseDto(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(404).body(res);
    }
}