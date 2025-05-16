package com.example.fintech.domain.trasactionRequest.dto;

import lombok.Getter;

@Getter
public class TransactionStatusUpdateDTO {

    private String status;
    public TransactionStatusUpdateDTO(String status) {
        this.status = status;
    }
}