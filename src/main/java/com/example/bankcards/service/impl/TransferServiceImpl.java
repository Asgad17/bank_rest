package com.example.bankcards.service.impl;

import com.example.bankcards.dto.transfer.request.TransferRequest;
import com.example.bankcards.dto.transfer.response.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.enums.TransferStatus;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    public TransferServiceImpl(CardRepository cardRepository, TransferRepository transferRepository) {
        this.cardRepository = cardRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    @Transactional
    public void transfer(TransferRequest request) {

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferException(
                    "Transfer amount must be greater than zero");
        }

        Card fromCard = cardRepository.findById(request.getFromCardId())
                .orElseThrow(() ->
                        new CardNotFoundException("Source card not found"));

        Card toCard = cardRepository.findById(request.getToCardId())
                .orElseThrow(() ->
                        new CardNotFoundException("Destination card not found"));

        if (fromCard.getId().equals(toCard.getId())) {
            throw new TransferException(
                    "Cannot transfer to the same card");
        }

        if (!fromCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new TransferException(
                    "Source card is not active");
        }

        if (!toCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new TransferException(
                    "Destination card is not active");
        }

        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new TransferException(
                    "Insufficient balance");
        }

        BigDecimal newFromBalance =
                fromCard.getBalance().subtract(request.getAmount());

        BigDecimal newToBalance =
                toCard.getBalance().add(request.getAmount());

        fromCard.setBalance(newFromBalance);
        toCard.setBalance(newToBalance);

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(request.getAmount())
                .status(TransferStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .balanceAfterFrom(newFromBalance)
                .balanceAfterTo(newToBalance)
                .build();

        transferRepository.save(transfer);
    }

    @Override
    public Page<TransferResponse> getStatement(Long cardId, LocalDate from, LocalDate to, int page,
            int size) {

        cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.plusDays(1).atStartOfDay();

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return transferRepository.findStatement(cardId, fromDateTime, toDateTime, pageable)
                .map(transfer -> {

                    TransferResponse response = new TransferResponse();
                    response.setDate(transfer.getCreatedAt());
                    response.setAmount(transfer.getAmount());

                    if (transfer.getFromCard().getId().equals(cardId)) {
                        response.setOperationType("TRANSFER_OUT");
                        response.setBalanceAfterOperation(transfer.getBalanceAfterFrom());
                    } else {
                        response.setOperationType("TRANSFER_IN");
                        response.setBalanceAfterOperation(transfer.getBalanceAfterTo());
                    }
                    return response;
                });
    }
}