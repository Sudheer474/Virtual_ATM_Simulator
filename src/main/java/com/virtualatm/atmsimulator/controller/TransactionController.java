package com.virtualatm.atmsimulator.controller;

import com.virtualatm.atmsimulator.dto.transaction.CardAuthRequest;
import com.virtualatm.atmsimulator.dto.transaction.CardTransferRequest;
import com.virtualatm.atmsimulator.dto.transaction.MiniStatementDTO;
import com.virtualatm.atmsimulator.dto.transaction.TransactionResponse;
import com.virtualatm.atmsimulator.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // WITHDRAW
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody CardAuthRequest req) {
        return ResponseEntity.ok(transactionService.withdraw(req));
    }

    // DEPOSIT
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody CardAuthRequest req) {
        return ResponseEntity.ok(transactionService.deposit(req));
    }

    // BALANCE ENQUIRY
    @PostMapping("/balance")
    public ResponseEntity<TransactionResponse> balance(@RequestBody CardAuthRequest req) {
        return ResponseEntity.ok(transactionService.checkBalance(req));
    }

    // TRANSFER
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody CardTransferRequest req) {
        return ResponseEntity.ok(transactionService.transfer(req));
    }

    // MINI STATEMENT
    @GetMapping("/mini-statement/{accountId}")
    public ResponseEntity<List<MiniStatementDTO>> getMiniStatement(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getMiniStatement(accountId));
    }
}
