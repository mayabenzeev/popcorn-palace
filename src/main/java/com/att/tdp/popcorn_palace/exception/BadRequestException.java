package com.att.tdp.popcorn_palace.exception;
/**
 * The type Bad request exception.
 * Throws an exception when there is an invalid input from the client.
 */
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
