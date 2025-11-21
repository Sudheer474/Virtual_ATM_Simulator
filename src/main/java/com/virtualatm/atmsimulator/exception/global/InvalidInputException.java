package com.virtualatm.atmsimulator.exception.global;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}

