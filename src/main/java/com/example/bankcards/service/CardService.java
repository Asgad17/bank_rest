package com.example.bankcards.service;

import com.example.bankcards.dto.card.request.CardCreateRequest;
import com.example.bankcards.dto.card.response.CardResponse;
import org.springframework.data.domain.Page;
import com.example.bankcards.enums.CardStatus;
import org.springframework.data.domain.Pageable;


public interface CardService {

    CardResponse findById(Long cardId);
    CardResponse createCard(CardCreateRequest request, Long userId);
    void blockCard(Long cardId);
    void activateCard(Long cardId);
    void deleteCard(Long cardId);
    Page<CardResponse> findByUserId(Long userId, CardStatus status, Pageable pageable);
}
