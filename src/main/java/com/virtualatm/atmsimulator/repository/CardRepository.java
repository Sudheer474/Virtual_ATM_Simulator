package com.virtualatm.atmsimulator.repository;

import com.virtualatm.atmsimulator.model.Card;
import com.virtualatm.atmsimulator.model.enums.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);
    List<Card> findByStatus(CardStatus status);

}
