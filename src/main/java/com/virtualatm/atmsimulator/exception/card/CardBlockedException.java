package com.virtualatm.atmsimulator.exception.card;

public class CardBlockedException extends RuntimeException {
    public CardBlockedException(String message) {
        super(message);
    }
}