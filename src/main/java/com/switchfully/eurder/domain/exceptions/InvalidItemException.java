package com.switchfully.eurder.domain.exceptions;

public class InvalidItemException extends RuntimeException {
    public InvalidItemException(String message) {
        super(message);
    }
}

