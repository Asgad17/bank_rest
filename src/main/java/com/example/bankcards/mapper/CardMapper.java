package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.request.CardCreateRequest;
import com.example.bankcards.dto.card.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toEntity(CardCreateRequest request) {
        Card card = new Card();

        card.setCardNumber(request.getCardNumber());
        card.setOwnerName(request.getOwnerName());
        card.setExpirationDate(request.getExpirationDate());
        card.setBalance(request.getBalance());
        card.setStatus(CardStatus.ACTIVE);
        return card;
    }

    public CardResponse toResponse(Card card) {
        CardResponse response = new CardResponse();

        response.setId(card.getId());
        response.setCardNumber(maskCardNumber(card.getCardNumber()));
        response.setOwnerName(card.getOwnerName());
        response.setExpirationDate(card.getExpirationDate());
        response.setStatus(card.getStatus());
        response.setBalance(card.getBalance());
        return response;
    }

    private String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}