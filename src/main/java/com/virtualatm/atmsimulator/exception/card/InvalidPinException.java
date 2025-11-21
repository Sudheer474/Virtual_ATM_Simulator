package com.virtualatm.atmsimulator.exception.card;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message) {
        super(message);
    }
}
