package com.virtualatm.atmsimulator.service.impl;

import com.virtualatm.atmsimulator.exception.account.AccountBlockedException;
import com.virtualatm.atmsimulator.exception.account.AccountNotFoundException;
import com.virtualatm.atmsimulator.exception.account.InsufficientBalanceException;
import com.virtualatm.atmsimulator.exception.global.InvalidInputException;
import com.virtualatm.atmsimulator.exception.user.UserNotFoundException;
import com.virtualatm.atmsimulator.model.Account;
import com.virtualatm.atmsimulator.model.Transaction;
import com.virtualatm.atmsimulator.model.User;
import com.virtualatm.atmsimulator.model.enums.AccountStatus;
import com.virtualatm.atmsimulator.model.enums.AccountType;
import com.virtualatm.atmsimulator.model.enums.TransactionStatus;
import com.virtualatm.atmsimulator.model.enums.TransactionType;
import com.virtualatm.atmsimulator.repository.AccountRepository;
import com.virtualatm.atmsimulator.repository.CardRepository;
import com.virtualatm.atmsimulator.repository.TransactionRepository;
import com.virtualatm.atmsimulator.repository.UserRepository;
import com.virtualatm.atmsimulator.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    private final BigDecimal ZERO = BigDecimal.ZERO;

    @Override
    @Transactional
    public Account createAccount(Long userId, AccountType type, BigDecimal initialDeposit) {
        if (initialDeposit == null || initialDeposit.compareTo(ZERO) < 0) {
            throw new InvalidInputException("Initial deposit must be non-negative");
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Account acc = Account.builder()
                .accountNumber(generateAccountNumber())
                .accountType(type)
                .balance(initialDeposit)
                .ifscCode("IFSC0001") // replace with branch logic if needed
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();

        Account saved = accountRepository.save(acc);

        // optional: create transaction record for the initial deposit
        if (initialDeposit.compareTo(ZERO) > 0) {
            Transaction txn = Transaction.builder()
                    .transactionRef("TXN-" + System.currentTimeMillis())
                    .transactionType(TransactionType.DEPOSIT)
                    .amount(initialDeposit)
                    .status(TransactionStatus.SUCCESS)
                    .description("Initial deposit")
                    .account(saved)
                    .transactionTime(LocalDateTime.now())
                    .build();
            transactionRepository.save(txn);
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Account is not active: " + accountNumber);
        }

        account.setBalance(account.getBalance().add(amount));
        Account saved = accountRepository.save(account);

        Transaction txn = Transaction.builder()
                .transactionRef("TXN-" + System.currentTimeMillis())
                .transactionType(TransactionType.DEPOSIT)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .description("Deposit")
                .account(saved)
                .transactionTime(LocalDateTime.now())
                .build();
        transactionRepository.save(txn);

        return saved;
    }

    @Override
    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("Withdrawal amount must be positive");
        }

        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Account is not active: " + accountNumber);
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for account: " + accountNumber);
        }

        account.setBalance(account.getBalance().subtract(amount));
        Account saved = accountRepository.save(account);

        Transaction txn = Transaction.builder()
                .transactionRef("TXN-" + System.currentTimeMillis())
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .description("ATM Withdrawal")
                .account(saved)
                .transactionTime(LocalDateTime.now())
                .build();
        transactionRepository.save(txn);

        return saved;
    }

    @Override
    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidInputException("Cannot transfer to the same account");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("Transfer amount must be positive");
        }

        // lock source and destination in a stable order to avoid deadlocks
        Account source = accountRepository.findByAccountNumberForUpdate(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found: " + fromAccountNumber));

        Account target = accountRepository.findByAccountNumberForUpdate(toAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Target account not found: " + toAccountNumber));

        if (source.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Source account is not active: " + fromAccountNumber);
        }
        if (target.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Target account is not active: " + toAccountNumber);
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient funds in account: " + fromAccountNumber);
        }

        // debit source
        source.setBalance(source.getBalance().subtract(amount));
        accountRepository.save(source);

        // credit target
        target.setBalance(target.getBalance().add(amount));
        accountRepository.save(target);

        // create two transactions: debit and credit
        Transaction debitTxn = Transaction.builder()
                .transactionRef("TXN-" + System.currentTimeMillis() + "-D")
                .transactionType(TransactionType.TRANSFER)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .description("Transfer to " + toAccountNumber)
                .account(source)
                .receiverAccount(target)
                .transactionTime(LocalDateTime.now())
                .build();
        transactionRepository.save(debitTxn);

        Transaction creditTxn = Transaction.builder()
                .transactionRef("TXN-" + System.currentTimeMillis() + "-C")
                .transactionType(TransactionType.TRANSFER)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .description("Transfer from " + fromAccountNumber)
                .account(target)
                .receiverAccount(source)
                .transactionTime(LocalDateTime.now())
                .build();
        transactionRepository.save(creditTxn);
    }

    // simple account number generator (replace with bank rule in future)
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}

