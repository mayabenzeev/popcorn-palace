package com.att.tdp.popcorn_palace.exception;
/**
 * The type Already Exist exception.
 * Throws an exception when trying to add an entity which is already in the system
 * (based on specific criteria)
 */
public class AlreadyExistException extends RuntimeException{

        public AlreadyExistException(String message) {
            super(message);
        }
}
