package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * The type Global exception handler.
 * Handles all exceptions in the application and returns a unified response format.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles not found exceptions such as trying to access missing entities
     * @param e - exception
     * @return ResponseEntity with 404 status
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return createResponseBody(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles already exist exceptions such as trying to add an existing entity
     * @param e - exception
     * @return ResponseEntity with 409 status
     */
    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<?> handleAlreadyExistException(AlreadyExistException e) {
        return createResponseBody(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handles logically validation errors such as overlapping showtimes, wrong dates, etc.
     * @param e - exception
     * @return ResponseEntity with 400 status
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return createResponseBody(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles input validation errors such as empty, null, or wrong format fields
     * @param e - exception
     * @return ResponseEntity with 400 status
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e) {
        // beautify all validation errors
        String errorMessage = e.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return createResponseBody(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles invalid JSON format exceptions such as missing or invalid fields (before validation)
     * @param e - exception
     * @return ResponseEntity with 404 status
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return createResponseBody("Invalid JSON format " +
                e.getMostSpecificCause(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions
     * @param e - exception
     * @return ResponseEntity with 500 status
     */
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
