package com.virtualatm.atmsimulator.mapper;

import com.virtualatm.atmsimulator.dto.card.CardDTO;
import com.virtualatm.atmsimulator.model.Card;
import com.virtualatm.atmsimulator.model.enums.CardStatus;
import com.virtualatm.atmsimulator.model.enums.CardType;

public class CardMapper {

    public static CardDTO toDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setCardNumber(card.getCardNumber());
        dto.setCvv(card.getCvv());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setPin(card.getPin());
        dto.setCardType(card.getCardType().name());
        dto.setStatus(card.getStatus().name());
        dto.setAccountId(card.getAccount() != null ? card.getAccount().getId() : null);
        return dto;
    }

    public static Card toEntity(CardDTO dto) {
        Card card = new Card();
        card.setId(dto.getId());
        card.setCardNumber(dto.getCardNumber());
        card.setCvv(dto.getCvv());
        card.setExpiryDate(dto.getExpiryDate());
        card.setPin(dto.getPin());
        card.setCardType(CardType.valueOf(dto.getCardType()));
        card.setStatus(CardStatus.valueOf(dto.getStatus()));

        return card; // attach Account in Service layer later
    }
}
