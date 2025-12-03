package com.virtualatm.atmsimulator.controller;

import com.virtualatm.atmsimulator.dto.card.CardDTO;
import com.virtualatm.atmsimulator.dto.card.ChangePinRequest;
import com.virtualatm.atmsimulator.dto.card.IssueCardRequest;
import com.virtualatm.atmsimulator.dto.card.ValidatePinRequest;
import com.virtualatm.atmsimulator.model.Card;
import com.virtualatm.atmsimulator.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // Issue Card
    @PostMapping("/{accountId}/issue")
    public ResponseEntity<CardDTO> issueCard(@PathVariable Long accountId, @RequestBody(required = false) IssueCardRequest request) {
        String pin = (request != null) ? request.getPin() : null;
        Card card = cardService.issueCard(accountId, pin);
        return ResponseEntity.ok(toDTO(card));
    }

    // Get Card by Account
    @GetMapping("/account/{accountId}")
    public ResponseEntity<CardDTO> getCardByAccount(@PathVariable Long accountId) {
        Card card = cardService.getCardByAccount(accountId);
        return ResponseEntity.ok(toDTO(card));
    }

    // Validate PIN
    @PostMapping("/validate-pin")
    public ResponseEntity<String> validatePin(@RequestBody ValidatePinRequest request) {
        cardService.validateCard(request.getCardNumber(), request.getPin());
        return ResponseEntity.ok("PIN validated successfully");
    }

    // Change PIN
    @PutMapping("/change-pin")
    public ResponseEntity<CardDTO> changePin(@RequestBody ChangePinRequest request) {
        Card card = cardService.changePin(
                request.getCardNumber(),
                request.getOldPin(),
                request.getNewPin()
        );
        return ResponseEntity.ok(toDTO(card));
    }

    // Block Card
    @PutMapping("/{cardNumber}/block")
    public ResponseEntity<CardDTO> blockCard(@PathVariable String cardNumber) {
        Card card = cardService.blockCard(cardNumber);
        return ResponseEntity.ok(toDTO(card));
    }

    // Activate Card
    @PutMapping("/{cardNumber}/activate")
    public ResponseEntity<CardDTO> activateCard(@PathVariable String cardNumber) {
        Card card = cardService.activateCard(cardNumber);
        return ResponseEntity.ok(toDTO(card));
    }

    // Helper: Convert Entity → DTO
    private CardDTO toDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setCardNumber(card.getCardNumber());
        dto.setCvv(card.getCvv());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setCardType(card.getCardType().name());
        dto.setStatus(card.getStatus().name());
        dto.setAccountId(card.getAccount().getId());
        return dto;
    }
}
