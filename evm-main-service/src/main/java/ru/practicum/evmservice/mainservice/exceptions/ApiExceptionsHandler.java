package ru.practicum.evmservice.mainservice.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionsHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .errors(Arrays
                                .stream(e.getStackTrace())
                                .map(StackTraceElement::toString)
                                .collect(Collectors.toList()))
                        .message(e.getMessage())
                        .reason("Integrity constraint has been violated.")
                        .status(HttpStatus.CONFLICT)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> String.format("Field: %s. Error: %s. Value: %s",
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .toList();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .errors(errors)
                        .message(e.getMessage())
                        .reason("Incorrectly made request.")
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ApiError> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .errors(Arrays
                                .stream(e.getStackTrace())
                                .map(StackTraceElement::toString)
                                .collect(Collectors.toList()))
                        .message(e.getMessage())
                        .reason(e.getCause().getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .message(e.getMessage())
                        .reason("The required object was not found.")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(e.getTimestamp())
                        .build());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .message(e.getMessage())
                        .reason("For the requested operation the conditions are not met.")
                        .status(HttpStatus.FORBIDDEN)
                        .timestamp(e.getTimestamp())
                        .build());
    }

    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<ApiError> handleIncorrectRequestException(IncorrectRequestException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .message(e.getMessage())
                        .reason("Incorrectly made request.")
                        .status(HttpStatus.CONFLICT)
                        .timestamp(e.getTimestamp())
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiError.builder()
                        .errors(Arrays
                                .stream(e.getStackTrace())
                                .map(StackTraceElement::toString)
                                .collect(Collectors.toList()))
                        .message(e.getMessage())
                        .reason("Integrity constraint has been violated.")
                        .status(HttpStatus.CONFLICT)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
