package com.virtualatm.atmsimulator.exception.user;


public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
