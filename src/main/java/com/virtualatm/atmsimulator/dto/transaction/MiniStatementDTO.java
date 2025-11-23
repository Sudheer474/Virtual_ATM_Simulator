package com.virtualatm.atmsimulator.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MiniStatementDTO {
    private String transactionRef;
    private String type;
    private BigDecimal amount;
    private String status;
    private LocalDateTime time;
    private String description;
}
