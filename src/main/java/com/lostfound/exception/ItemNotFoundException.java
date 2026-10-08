package com.lostfound.exception;

/**
 * Custom Exception thrown when an item is not found or is inaccessible.
 */
public class ItemNotFoundException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
