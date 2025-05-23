package com.example.fintech.domain.transaction.converter;


import com.example.fintech.domain.account.entity.Account;
import com.example.fintech.domain.transaction.dto.request.TransactionRequestDTO;
import com.example.fintech.domain.transaction.dto.response.TransactionResponse;
import com.example.fintech.domain.transaction.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter {

    // dto -> entity
    public Transaction toEntity(TransactionRequestDTO dto, Account account, Long categoryId) {

        return Transaction.builder()
                .account(account)
                .timestamp(dto.getTimestamp())
                .merchantName(dto.getMerchantName())
                .amount(-Math.abs(dto.getAmount()))
                .categoryId(categoryId)
                .build();
    }

    // failureCategoryDTO 
    public TransactionResponse.failureCategoryDTO toFailureCategoryDTO(TransactionRequestDTO dto, String reason) {
        return TransactionResponse.failureCategoryDTO.builder()
                .reason(reason)
                .merchantName(dto.getMerchantName())
                .mccCode(dto.getMccCode())
                .amount(dto.getAmount())
                .timestamp(dto.getTimestamp().toString())
                .userId(dto.getUserId())
                .build();
    }



}
