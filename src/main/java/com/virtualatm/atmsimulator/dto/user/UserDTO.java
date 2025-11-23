package com.virtualatm.atmsimulator.dto.user;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String createdAt;
}
