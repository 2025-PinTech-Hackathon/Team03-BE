package com.example.fintech.domain.transaction.converter;

import com.example.fintech.domain.transaction.dto.response.HistoryResponse;
import com.example.fintech.domain.transaction.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoryConverter {

    // Entity -> HistoryResponse DTO
    public HistoryResponse.HistoryResponseDTO toResponseDTO(List<Transaction> transactions) {
        List<HistoryResponse.TransactionDTO> transactionDTOs = transactions.stream()
                .map(tx -> HistoryResponse.TransactionDTO.builder()
                        .merchantName(tx.getMerchantName())
                        .amount(tx.getAmount())
                        .timestamp(tx.getTimestamp())
                        .build())
                .toList();

        return HistoryResponse.HistoryResponseDTO.builder()
                .history(transactionDTOs)
                .build();
    }
}