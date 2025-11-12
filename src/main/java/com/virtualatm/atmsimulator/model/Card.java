package com.virtualatm.atmsimulator.model;

import com.virtualatm.atmsimulator.model.enums.CardStatus;
import com.virtualatm.atmsimulator.model.enums.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String cardNumber; // e.g., "4215 6789 1234 5678"

    @Column(nullable = false, length = 3)
    private String cvv; // e.g., "123"

    @Column(nullable = false)
    private LocalDate expiryDate; // e.g., 2029-05-01

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$", message = "PIN must be 4 digits")
    @Column(nullable = false)
    private String pin; // will encrypt later

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType; // DEBIT or CREDIT (for now only DEBIT)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status; // ACTIVE, BLOCKED, EXPIRED

    //Relationship: One card belongs to one account
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
}
