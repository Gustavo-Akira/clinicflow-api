package com.clinicflow.shared.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleViolationException.class)
    ResponseEntity<ApiError> handleBusinessRuleViolation(
        BusinessRuleViolationException exception,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(buildError(
            exception.getCode(),
            exception.getMessage(),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        ));
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        BindException.class,
        ConstraintViolationException.class
    })
    ResponseEntity<ApiError> handleValidationFailures(Exception exception, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(buildError(
            "VALIDATION_ERROR",
            extractValidationMessage(exception),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleUnexpectedError(Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(
            "INTERNAL_ERROR",
            "An unexpected error occurred.",
            HttpStatus.INTERNAL_SERVER_ERROR,
            request.getRequestURI()
        ));
    }

    private ApiError buildError(String code, String message, HttpStatus status, String path) {
        return new ApiError(code, message, status.value(), path, OffsetDateTime.now());
    }

    private String extractValidationMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        }
        if (exception instanceof BindException bindException) {
            return bindException.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        }
        return exception.getMessage();
    }
}
