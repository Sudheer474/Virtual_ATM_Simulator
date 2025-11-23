package com.virtualatm.atmsimulator.mapper;

import com.virtualatm.atmsimulator.dto.user.*;
import com.virtualatm.atmsimulator.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCreatedAt(user.getCreatedAt().toString());
        return dto;
    }

    public static User toEntity(CreateUserRequest req) {
        return User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .password(req.getPassword())
                .build();
    }
}
