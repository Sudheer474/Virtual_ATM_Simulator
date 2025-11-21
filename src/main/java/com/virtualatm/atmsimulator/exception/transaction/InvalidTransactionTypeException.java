package com.virtualatm.atmsimulator.exception.transaction;

public class InvalidTransactionTypeException extends RuntimeException {
    public InvalidTransactionTypeException(String message) {
        super(message);
    }
}
