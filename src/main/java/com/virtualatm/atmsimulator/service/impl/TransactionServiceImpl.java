package com.virtualatm.atmsimulator.service.impl;

import com.virtualatm.atmsimulator.dto.transaction.CardAuthRequest;
import com.virtualatm.atmsimulator.dto.transaction.CardTransferRequest;
import com.virtualatm.atmsimulator.dto.transaction.MiniStatementDTO;
import com.virtualatm.atmsimulator.dto.transaction.TransactionResponse;
import com.virtualatm.atmsimulator.exception.account.AccountBlockedException;
import com.virtualatm.atmsimulator.exception.account.AccountNotFoundException;
import com.virtualatm.atmsimulator.exception.account.InsufficientBalanceException;
import com.virtualatm.atmsimulator.mapper.TransactionMapper;
import com.virtualatm.atmsimulator.model.*;
import com.virtualatm.atmsimulator.model.enums.AccountStatus;
import com.virtualatm.atmsimulator.model.enums.TransactionStatus;
import com.virtualatm.atmsimulator.model.enums.TransactionType;
import com.virtualatm.atmsimulator.repository.AccountRepository;
import com.virtualatm.atmsimulator.repository.TransactionRepository;
import com.virtualatm.atmsimulator.service.CardService;
import com.virtualatm.atmsimulator.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    // DEPOSIT
    @Override
    @Transactional
    public TransactionResponse deposit(CardAuthRequest req) {

        var card = cardService.validateCard(req.getCardNumber(), req.getPin());
        var acc = accountRepository.findByAccountNumberForUpdate(card.getAccount().getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (acc.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active. Current status: " + acc.getStatus());
        }

        acc.setBalance(acc.getBalance().add(req.getAmount()));
        accountRepository.save(acc);

        Transaction txn = saveTxn(
                acc,
                TransactionType.DEPOSIT,
                req.getAmount(),
                "ATM Cash Deposit",
                null
        );

        return buildResponse(txn, acc.getBalance());
    }


    // WITHDRAW
    @Override
    @Transactional
    public TransactionResponse withdraw(CardAuthRequest req) {

        var card = cardService.validateCard(req.getCardNumber(), req.getPin());
        var acc = accountRepository.findByAccountNumberForUpdate(card.getAccount().getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (acc.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active. Current status: " + acc.getStatus());
        }


        if (acc.getBalance().compareTo(req.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance().subtract(req.getAmount()));
        accountRepository.save(acc);

        Transaction txn = saveTxn(
                acc,
                TransactionType.WITHDRAWAL,
                req.getAmount(),
                "ATM Cash Withdrawal",
                null
        );

        return buildResponse(txn, acc.getBalance());
    }


    // BALANCE INQUIRY
    @Override
    public TransactionResponse checkBalance(CardAuthRequest req) {

        var card = cardService.validateCard(req.getCardNumber(), req.getPin());
        var acc = card.getAccount();

        Transaction txn = saveTxn(
                acc,
                TransactionType.BALANCE_CHECK,
                BigDecimal.ZERO,
                "Balance Enquiry",
                null
        );

        return buildResponse(txn, acc.getBalance());
    }


    // TRANSFER
    @Override
    @Transactional
    public TransactionResponse transfer(CardTransferRequest req) {

        var card = cardService.validateCard(req.getFromCardNumber(), req.getPin());
        var sender = accountRepository.findByAccountNumberForUpdate(card.getAccount().getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        var receiver = accountRepository.findByAccountNumber(req.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if (sender.getBalance().compareTo(req.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        sender.setBalance(sender.getBalance().subtract(req.getAmount()));
        receiver.setBalance(receiver.getBalance().add(req.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction txn = saveTxn(
                sender,
                TransactionType.TRANSFER,
                req.getAmount(),
                "Transfer to " + receiver.getAccountNumber(),
                receiver
        );

        return buildResponse(txn, sender.getBalance());
    }


    // MINI STATEMENT
    @Override
    public List<MiniStatementDTO> getMiniStatement(Long accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        return transactionRepository
                .findTop10ByAccountIdOrderByTransactionTimeDesc(accountId)
                .stream()
                .map(TransactionMapper::toMiniStatementDTO)
                .toList();
    }


    // Helper: Save Transaction
    private Transaction saveTxn(
            Account acc,
            TransactionType type,
            BigDecimal amount,
            String description,
            Account receiver
    ) {
        Transaction txn = Transaction.builder()
                .transactionRef("TXN-" + System.currentTimeMillis())
                .transactionType(type)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .description(description)
                .account(acc)
                .receiverAccount(receiver)
                .transactionTime(LocalDateTime.now())
                .build();

        return transactionRepository.save(txn);
    }


    // Helper: Build Response DTO
    private TransactionResponse buildResponse(Transaction txn, BigDecimal updatedBalance) {
        return TransactionResponse.builder()
                .transactionRef(txn.getTransactionRef())
                .transactionType(txn.getTransactionType().name())
                .amount(txn.getAmount())
                .description(txn.getDescription())
                .status(txn.getStatus().name())
                .time(txn.getTransactionTime())
                .updatedBalance(updatedBalance)
                .build();
    }
}
