package com.virtualatm.atmsimulator.service;

import com.virtualatm.atmsimulator.dto.UserDTO;
import com.virtualatm.atmsimulator.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDTO userDTO);

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User updateUser(Long userId, UserDTO userDTO);

    void deleteUser(Long userId); // soft delete

    List<User> getAllUsers();

    void changePassword(Long userId, String oldPassword, String newPassword);
}
