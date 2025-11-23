package com.virtualatm.atmsimulator.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private String phoneNumber;
}
