package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return createResponseBody(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<?> handleAlreadyExistException(AlreadyExistException e) {
        return createResponseBody(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return createResponseBody(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a unified response format for all exceptions
     * @param e - exception
     * @param status - HTTP status
     * @return ResponseEntity
     */
    private ResponseEntity<?> createResponseBody (Exception e, HttpStatus status) {
        Map<String, Object> responseBody = new LinkedHashMap<>(); // to keep this order of fields in the response
        responseBody.put("timestamp", ZonedDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("message", e.getMessage());

        return new ResponseEntity(responseBody, status);
    }
}
