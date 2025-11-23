package com.virtualatm.atmsimulator.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardTransferRequest {
    private String fromCardNumber;
    private String pin;
    private String toAccountNumber;
    private BigDecimal amount;
}
