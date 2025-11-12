package com.virtualatm.atmsimulator.model;

import com.virtualatm.atmsimulator.model.enums.TransactionStatus;
import com.virtualatm.atmsimulator.model.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique transaction reference number (e.g., "TXN20241110123000123")
    @Column(unique = true, nullable = false)
    private String transactionRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status; // SUCCESS, FAILED, PENDING

    private String description;

    //Relationship: Many transactions → One account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // For transfer transactions: optional receiver account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;

    @CreationTimestamp
    private LocalDateTime transactionTime;
}

