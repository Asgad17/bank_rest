package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.request.CardCreateRequest;
import com.example.bankcards.dto.card.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.example.bankcards.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;

    public CardServiceImpl(CardRepository cardRepository,
                           CardMapper cardMapper,
                           UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CardResponse findById(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() ->
                        new CardNotFoundException("Card not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (currentUser.getRole() == Role.USER
                && !card.getUser().getId().equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "You cannot view another user's card"
            );
        }

        return cardMapper.toResponse(card);
    }

    @Override
    public CardResponse createCard(CardCreateRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Card card = cardMapper.toEntity(request);
        card.setUser(user);
        Card savedCard = cardRepository.save(card);
        return cardMapper.toResponse(savedCard);
    }

    @Override
    public void blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    public void activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        cardRepository.delete(card);
    }

    @Override
    public Page<CardResponse> findByUserId(
            Long userId,
            CardStatus status,
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (currentUser.getRole() == Role.USER
                && !currentUser.getId().equals(userId)) {

            throw new AccessDeniedException(
                    "You cannot view another user's cards"
            );
        }

        Page<Card> cards;

        if (status != null) {
            cards = cardRepository.findByUserIdAndStatus(
                    userId, status, pageable);
        } else {
            cards = cardRepository.findByUserId(
                    userId, pageable);
        }

        return cards.map(cardMapper::toResponse);
    }

}