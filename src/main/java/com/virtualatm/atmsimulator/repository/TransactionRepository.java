package com.virtualatm.atmsimulator.repository;

import com.virtualatm.atmsimulator.model.Transaction;
import com.virtualatm.atmsimulator.model.enums.TransactionStatus;
import com.virtualatm.atmsimulator.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByStatus(TransactionStatus status);

}
