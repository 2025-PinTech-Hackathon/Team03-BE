package com.example.fintech.domain.trasactionRequest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class TransactionReqResponseDTO {
    private String reason;
    private String merchantName;
    private int amount;
    private String timestamp;

}