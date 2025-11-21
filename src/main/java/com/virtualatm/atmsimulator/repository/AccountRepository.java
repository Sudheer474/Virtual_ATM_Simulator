package com.virtualatm.atmsimulator.repository;

import com.virtualatm.atmsimulator.model.Account;
import com.virtualatm.atmsimulator.model.enums.AccountStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNumber = :accNo")
    Optional<Account> findByAccountNumberForUpdate(@Param("accNo") String accountNumber);

    List<Account> findByUserId(Long userId);

    List<Account> findByStatus(AccountStatus status);
}

