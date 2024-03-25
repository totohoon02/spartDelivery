package com.sparta.spartdelivery.exception;

public class CartConflictException extends RuntimeException {
    public CartConflictException(String message) {
        super(message);
    }
}
