package com.virtualatm.atmsimulator.mapper;

import com.virtualatm.atmsimulator.dto.transaction.MiniStatementDTO;
import com.virtualatm.atmsimulator.model.Transaction;

public class TransactionMapper {

    public static MiniStatementDTO toMiniStatementDTO(Transaction t) {
        MiniStatementDTO dto = new MiniStatementDTO();
        dto.setTransactionRef(t.getTransactionRef());
        dto.setType(t.getTransactionType().name());
        dto.setAmount(t.getAmount());
        dto.setStatus(t.getStatus().name());
        dto.setTime(t.getTransactionTime());
        dto.setDescription(t.getDescription());
        return dto;
    }
}
