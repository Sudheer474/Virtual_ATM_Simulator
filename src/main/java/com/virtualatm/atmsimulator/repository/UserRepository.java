package com.virtualatm.atmsimulator.repository;

import com.virtualatm.atmsimulator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNumber(String mobileNumber);

}
