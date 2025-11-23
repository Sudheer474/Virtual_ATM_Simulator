package com.virtualatm.atmsimulator.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardAuthRequest {
    private String cardNumber;
    private String pin;
    private BigDecimal amount; // not required for balance enquiry
}

