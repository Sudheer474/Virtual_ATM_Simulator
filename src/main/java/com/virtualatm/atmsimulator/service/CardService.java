package com.virtualatm.atmsimulator.service;

import com.virtualatm.atmsimulator.model.Card;

public interface CardService {
    Card issueCard(Long accountId, String rawPin); // rawPin optional; if null generate
    Card validateCard(String cardNumber, String pin); // throws exceptions if invalid
    Card changePin(String cardNumber, String oldPin, String newPin);
    Card blockCard(String cardNumber);
    Card activateCard(String cardNumber);
    Card getCardByAccount(Long accountId);
}

