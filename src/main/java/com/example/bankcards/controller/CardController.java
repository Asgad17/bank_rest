package com.example.bankcards.controller;

import com.example.bankcards.dto.card.request.CardCreateRequest;
import com.example.bankcards.dto.card.response.CardResponse;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CardResponse>> getUserCards(
            @PathVariable Long userId,
            @RequestParam(required = false) CardStatus status,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        return ResponseEntity.ok(cardService.findByUserId(userId, status, pageable));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCardById(
            @PathVariable Long cardId) {

        return ResponseEntity.ok(cardService.findById(cardId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<CardResponse> createCard(
            @RequestBody CardCreateRequest request,
            @PathVariable Long userId) {

        return ResponseEntity.ok(cardService.createCard(request, userId));
    }

    @PatchMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cardId}/activate")
    public ResponseEntity<Void> activateCard(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}