package com.example.fintech.domain.transaction.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class HistoryResponse {

    @Getter
    @Builder
    public static class HistoryResponseDTO{
        private List<TransactionDTO> history;

    }

    @Getter
    @Builder
    public static class TransactionDTO {
        private String merchantName;
        private int amount;
        private LocalDateTime timestamp;
    }

}
