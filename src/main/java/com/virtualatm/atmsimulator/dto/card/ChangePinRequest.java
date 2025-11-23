package com.virtualatm.atmsimulator.dto.card;

import lombok.Data;

@Data
public class ChangePinRequest {
    private String cardNumber;
    private String oldPin;
    private String newPin;
}
