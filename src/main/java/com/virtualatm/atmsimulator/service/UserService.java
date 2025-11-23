package com.virtualatm.atmsimulator.service;

import com.virtualatm.atmsimulator.dto.user.UserDTO;
import com.virtualatm.atmsimulator.dto.user.CreateUserRequest;
import com.virtualatm.atmsimulator.dto.user.UpdateUserRequest;
import com.virtualatm.atmsimulator.dto.user.UserDTO;
import com.virtualatm.atmsimulator.model.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(CreateUserRequest req);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UpdateUserRequest req);
    void deleteUser(Long id);

//    UserDTO getUserByEmail(String email);
//    void changePassword(Long userId, String oldPassword, String newPassword);

}