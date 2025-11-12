package com.virtualatm.atmsimulator.runner;

import com.virtualatm.atmsimulator.model.*;
import com.virtualatm.atmsimulator.model.enums.*;
import com.virtualatm.atmsimulator.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DbTestRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public DbTestRunner(UserRepository userRepository,
                        AccountRepository accountRepository,
                        CardRepository cardRepository,
                        TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== Starting DB Test Runner ===");

        // 1. create user
        User user = User.builder()
                .fullName("Test User")
                .email("testuser@example.com")
                .phoneNumber("9999999999")
                .password("password123") // in production you’d encrypt
                .build();
        user = userRepository.save(user);
        System.out.println("Created User: " + user);

        // 2. create account for user
        Account account = Account.builder()
                .accountNumber("ACC-" + System.currentTimeMillis())
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("1000.00"))
                .ifscCode("IFSC0001")
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();
        account = accountRepository.save(account);
        System.out.println("Created Account: " + account);

        // 3. create card for account
        Card card = Card.builder()
                .cardNumber("4000123412341234")
                .cvv("123")
                .expiryDate(LocalDate.now().plusYears(3))
                .pin("0000")
                .cardType(CardType.DEBIT)
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();
        card = cardRepository.save(card);
        System.out.println("Created Card: " + card);

        // 4. create transaction for account (deposit)
        Transaction transaction = Transaction.builder()
                .transactionRef("TXN-" + System.currentTimeMillis())
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("500.00"))
                .status(TransactionStatus.SUCCESS)
                .description("Initial deposit")
                .account(account)
                .build();
        transaction = transactionRepository.save(transaction);
        System.out.println("Created Transaction: " + transaction);

        System.out.println("=== DB Test Runner completed ===");
    }
}
