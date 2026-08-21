package com.example.bankcards.dto.transfer.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransferResponse {
    private LocalDateTime date;
    private String operationType;
    private BigDecimal amount;
    private BigDecimal balanceAfterOperation;
}