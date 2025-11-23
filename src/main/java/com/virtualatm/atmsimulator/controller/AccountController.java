package com.virtualatm.atmsimulator.controller;

import com.virtualatm.atmsimulator.dto.account.AccountResponse;
import com.virtualatm.atmsimulator.model.enums.AccountType;
import com.virtualatm.atmsimulator.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Create New Account for User
    @PostMapping("/create")
    public AccountResponse createAccount(
            @RequestParam Long userId,
            @RequestParam AccountType type,
            @RequestParam BigDecimal initialDeposit
    ) {
        var acc = accountService.createAccount(userId, type, initialDeposit);
        return mapToResponse(acc);
    }

    //Get Account by Number
    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(@PathVariable String accountNumber) {
        var acc = accountService.getAccountByNumber(accountNumber);
        return mapToResponse(acc);
    }

    //Get All Accounts of a User
    @GetMapping("/user/{userId}")
    public List<AccountResponse> getUserAccounts(@PathVariable Long userId) {
        return accountService.getAccountsByUser(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //Deposit
    @PostMapping("/deposit")
    public AccountResponse deposit(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount
    ) {
        var acc = accountService.deposit(accountNumber, amount);
        return mapToResponse(acc);
    }

    // Withdraw
    @PostMapping("/withdraw")
    public AccountResponse withdraw(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount
    ) {
        var acc = accountService.withdraw(accountNumber, amount);
        return mapToResponse(acc);
    }

    //Transfer
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam BigDecimal amount
    ) {
        accountService.transfer(fromAccount, toAccount, amount);
        return "Transfer successful!";
    }

    // DTO Mapper
    private AccountResponse mapToResponse(com.virtualatm.atmsimulator.model.Account acc) {
        AccountResponse resp = new AccountResponse();
        resp.setAccountId(acc.getId());
        resp.setBalance(acc.getBalance().doubleValue());
        resp.setHolderName(acc.getUser().getFullName());
        resp.setEmail(acc.getUser().getEmail());
        resp.setPhone(acc.getUser().getPhoneNumber());
        return resp;
    }
}
