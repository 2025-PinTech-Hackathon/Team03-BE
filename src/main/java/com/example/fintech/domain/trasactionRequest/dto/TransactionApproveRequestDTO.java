package com.example.fintech.domain.trasactionRequest.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionApproveRequestDTO {
    private String reason;
    private String merchantName;
    private int amount;
    private LocalDateTime timestamp;
    private String status;
}
