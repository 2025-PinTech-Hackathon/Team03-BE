package com.example.fintech.domain.trasactionRequest.service;

import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;

public interface TransactionRequestService {
    void saveTransaction(TransactionRequest entity);
}
