package com.att.tdp.popcorn_palace.exception;

/**
 * The type Not found exception.
 * Throws an exception when an entity is not found in the system
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}

//TODO: exception handler