package com.virtualatm.atmsimulator.service.impl;

import com.virtualatm.atmsimulator.dto.user.*;
import com.virtualatm.atmsimulator.exception.user.UserNotFoundException;
import com.virtualatm.atmsimulator.mapper.UserMapper;
import com.virtualatm.atmsimulator.model.User;
import com.virtualatm.atmsimulator.repository.UserRepository;
import com.virtualatm.atmsimulator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(CreateUserRequest req) {

        User user = UserMapper.toEntity(req);

        userRepository.save(user);

        return UserMapper.toDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));

        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest req) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setFullName(req.getFullName());
        user.setPhoneNumber(req.getPhoneNumber());

        userRepository.save(user);

        return UserMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }
}
