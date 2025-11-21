package com.virtualatm.atmsimulator.service.impl;

import com.virtualatm.atmsimulator.dto.UserDTO;
import com.virtualatm.atmsimulator.exception.user.DuplicateEmailException;
import com.virtualatm.atmsimulator.exception.user.DuplicatePhoneException;
import com.virtualatm.atmsimulator.exception.user.UserNotFoundException;
import com.virtualatm.atmsimulator.model.User;
import com.virtualatm.atmsimulator.repository.UserRepository;
import com.virtualatm.atmsimulator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 1. CREATE USER (using UserDTO)
    @Override
    public User createUser(UserDTO userDTO) {

        // Email check
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already registered: " + userDTO.getEmail());
        }

        // Phone check
        if (userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            throw new DuplicatePhoneException("Phone already registered: " + userDTO.getPhoneNumber());
        }

        // Map DTO -> Entity
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }


    // 2. GET USER BY ID
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with ID: " + userId)
                );
    }

    // ----------------------------------------------------------
    // 3. GET USER BY EMAIL
    // ----------------------------------------------------------
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with email: " + email)
                );
    }

    // ----------------------------------------------------------
    // 4. GET ALL USERS
    // ----------------------------------------------------------
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ----------------------------------------------------------
    // 5. UPDATE USER (using UserDTO)
    // ----------------------------------------------------------
    @Override
    public User updateUser(Long userId, UserDTO dto) {

        User existing = getUserById(userId);

        // Only validate if email is changed
        if (!existing.getEmail().equals(dto.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already taken: " + dto.getEmail());
            }
        }

        // Only validate if phone is changed
        if (!existing.getPhoneNumber().equals(dto.getPhoneNumber())) {
            if (userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new DuplicatePhoneException("Phone already taken: " + dto.getPhoneNumber());
            }
        }

        // Update entity
        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existing);
    }

    // ----------------------------------------------------------
    // 6. DELETE USER (Soft delete optional — currently hard delete)
    // ----------------------------------------------------------
    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    // ----------------------------------------------------------
    // 7. CHANGE PASSWORD
    // ----------------------------------------------------------
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {

        User user = getUserById(userId);

        // old password check
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // encode new password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
