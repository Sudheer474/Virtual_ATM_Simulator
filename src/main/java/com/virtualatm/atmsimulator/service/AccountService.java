package com.virtualatm.atmsimulator.service;

import com.virtualatm.atmsimulator.model.Account;
import com.virtualatm.atmsimulator.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account createAccount(Long userId, AccountType type, BigDecimal initialDeposit);

    Account getAccountByNumber(String accountNumber);

    List<Account> getAccountsByUser(Long userId);

    Account deposit(String accountNumber, BigDecimal amount);

    Account withdraw(String accountNumber, BigDecimal amount);

    void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);
}
