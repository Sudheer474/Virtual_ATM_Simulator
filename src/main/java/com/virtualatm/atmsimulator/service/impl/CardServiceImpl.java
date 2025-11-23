package com.virtualatm.atmsimulator.service.impl;

import com.virtualatm.atmsimulator.exception.account.AccountNotFoundException;
import com.virtualatm.atmsimulator.exception.card.*;
import com.virtualatm.atmsimulator.model.Card;
import com.virtualatm.atmsimulator.model.enums.CardStatus;
import com.virtualatm.atmsimulator.model.enums.CardType;
import com.virtualatm.atmsimulator.repository.AccountRepository;
import com.virtualatm.atmsimulator.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements com.virtualatm.atmsimulator.service.CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Override
    @Transactional
    public Card issueCard(Long accountId, String rawPin) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        if (account.getCard() != null) {
            throw new CardNotFoundException("Account already has a card");
        }

        String cardNumber = generateCardNumber();
        String cvv = String.format("%03d", random.nextInt(1000));
        LocalDate expiry = LocalDate.now().plusYears(3);

        String pinToStore = (rawPin == null || rawPin.isBlank())
                ? String.format("%04d", random.nextInt(10000))
                : rawPin;

        String hashedPin = passwordEncoder.encode(pinToStore);

        Card card = Card.builder()
                .cardNumber(cardNumber)
                .cvv(cvv)
                .expiryDate(expiry)
                .pin(hashedPin)
                .cardType(CardType.DEBIT)
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();

        Card saved = cardRepository.save(card);
        account.setCard(saved);
        accountRepository.save(account);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Card validateCard(String cardNumber, String pin) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardNumber));

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardBlockedException("Card is blocked");
        }
        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CardExpiredException("Card expired");
        }
        if (!passwordEncoder.matches(pin, card.getPin())) {
            throw new InvalidPinException("Invalid PIN");
        }
        return card;
    }

    @Override
    @Transactional
    public Card changePin(String cardNumber, String oldPin, String newPin) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardNumber));

        if (!passwordEncoder.matches(oldPin, card.getPin())) {
            throw new InvalidPinException("Old PIN incorrect");
        }

        card.setPin(passwordEncoder.encode(newPin));
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public Card blockCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardNumber));

        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public Card activateCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardNumber));

        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Card getCardByAccount(Long accountId) {

        return cardRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CardNotFoundException("Card not found for account: " + accountId));
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder("4");
        for (int i = 0; i < 15; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }
}