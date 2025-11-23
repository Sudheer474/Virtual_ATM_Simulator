package com.virtualatm.atmsimulator.dto.account;

import lombok.Data;

@Data
public class AccountResponse {
    private Long accountId;
    private String holderName;
    private String email;
    private String phone;
    private double balance;
}
