package com.virtualatm.atmsimulator.exception.user;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException(String msg) {
        super(msg);
    }
}
