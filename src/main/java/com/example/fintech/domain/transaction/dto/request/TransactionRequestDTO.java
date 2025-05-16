package com.example.fintech.domain.transaction.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequestDTO {
    private String merchantName;
    private int mccCode;
    private int amount;
    private LocalDateTime timestamp;
    private Long userId;
}
