package com.virtualatm.atmsimulator.dto.card;

import lombok.Data;

@Data
public class ValidatePinRequest {
    private String cardNumber;
    private String pin;
}

