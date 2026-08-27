package com.example.bankcards.controller;

import com.example.bankcards.dto.transfer.request.TransferRequest;
import com.example.bankcards.dto.transfer.response.TransferResponse;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody TransferRequest request) {

        transferService.transfer(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/card/{cardId}/statement")
    public ResponseEntity<Page<TransferResponse>> getStatement(
            @PathVariable Long cardId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(transferService.getStatement(cardId, from, to, page, size));
    }
}