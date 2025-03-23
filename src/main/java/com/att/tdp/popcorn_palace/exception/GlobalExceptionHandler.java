package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return createResponseBody(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<?> handleAlreadyExistException(AlreadyExistException e) {
        return createResponseBody(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return createResponseBody(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e) {
        // beautify all validation errors
        String errorMessage = e.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return createResponseBody(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return createResponseBody(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a unified response format for all exceptions
     * @param errorMessage - string exception message
     * @param status - HTTP status
     * @return ResponseEntity
     */
    private ResponseEntity<?> createResponseBody (String errorMessage, HttpStatus status) {
        Map<String, Object> responseBody = new LinkedHashMap<>(); // to keep this order of fields in the response
        responseBody.put("timestamp", ZonedDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("message", errorMessage);

        return new ResponseEntity(responseBody, status);
    }
}
