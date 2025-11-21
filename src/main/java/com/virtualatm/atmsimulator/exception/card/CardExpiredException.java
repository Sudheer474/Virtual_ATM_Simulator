package com.virtualatm.atmsimulator.exception.card;

public class CardExpiredException extends RuntimeException {
    public CardExpiredException(String message) {
        super(message);
    }
}
