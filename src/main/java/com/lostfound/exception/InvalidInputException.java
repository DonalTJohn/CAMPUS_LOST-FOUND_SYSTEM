package com.lostfound.exception;

/**
 * Custom Exception thrown when client or server inputs fail validation constraints.
 */
public class InvalidInputException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
