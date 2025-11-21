package com.virtualatm.atmsimulator.exception.transaction;

public class DailyLimitExceededException extends RuntimeException {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}
