package com.virtualatm.atmsimulator.service;

import com.virtualatm.atmsimulator.dto.transaction.CardAuthRequest;
import com.virtualatm.atmsimulator.dto.transaction.CardTransferRequest;
import com.virtualatm.atmsimulator.dto.transaction.MiniStatementDTO;
import com.virtualatm.atmsimulator.dto.transaction.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse deposit(CardAuthRequest req);

    TransactionResponse withdraw(CardAuthRequest req);

    TransactionResponse checkBalance(CardAuthRequest req);

    TransactionResponse transfer(CardTransferRequest req);

    List<MiniStatementDTO> getMiniStatement(Long accountId);
}
