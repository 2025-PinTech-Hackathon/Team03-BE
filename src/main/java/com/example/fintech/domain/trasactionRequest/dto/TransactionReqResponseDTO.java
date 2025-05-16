package com.example.fintech.domain.trasactionRequest.dto;

import lombok.Setter;

import java.time.LocalDateTime;

@Setter
public class TransactionReqResponseDTO {
    private String reason;
    private String merchantName;
    private int amount;
    private LocalDateTime timestamp;

}