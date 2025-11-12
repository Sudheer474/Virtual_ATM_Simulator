package com.virtualatm.atmsimulator.model;

import com.virtualatm.atmsimulator.model.enums.AccountStatus;
import com.virtualatm.atmsimulator.model.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber; // e.g. "SB-1234567890"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType; // SAVINGS, CURRENT

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String ifscCode; // Example: "HDFC0001234"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status; // ACTIVE, BLOCKED, CLOSED

    //Relationship: Many accounts → One user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Relationship: One account -> One card
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Card card;

    //Relationship: One account → Many transactions
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

