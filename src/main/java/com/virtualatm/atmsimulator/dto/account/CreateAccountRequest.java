package com.virtualatm.atmsimulator.dto.account;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String holderName;
    private String email;
    private String phone;
    private double initialBalance;
    private String pin;
}
