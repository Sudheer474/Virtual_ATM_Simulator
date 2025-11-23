package com.virtualatm.atmsimulator.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {
    private Long id;
    private String cardNumber;
    private String cvv;
    private LocalDate expiryDate;
    private String pin;
    private String cardType;
    private String status;
    private Long accountId;
}

