package com.virtualatm.atmsimulator.dto.account;

import lombok.Data;

@Data
public class UpdatePinRequest {
    private String oldPin;
    private String newPin;
}
