package com.virtualatm.atmsimulator.dto.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private String transactionRef;
    private String transactionType;
    private BigDecimal amount;
    private String status;
    private String description;
    private LocalDateTime time;
    private BigDecimal updatedBalance;
}
