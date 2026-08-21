package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.request.TransferRequest;
import com.example.bankcards.dto.transfer.response.TransferResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;


public interface TransferService {

    void transfer(TransferRequest request);
    Page<TransferResponse> getStatement(Long cardId, LocalDate from, LocalDate to, int page, int size);
}