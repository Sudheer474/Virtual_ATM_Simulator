package com.virtualatm.atmsimulator.exception.account;

public class AccountBlockedException extends RuntimeException {
    public AccountBlockedException(String message) {
        super(message);
    }
}
