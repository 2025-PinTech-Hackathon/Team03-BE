package com.example.fintech.domain.transaction.service;

import com.example.fintech.domain.transaction.dto.request.TransactionRequestDTO;
import com.example.fintech.domain.transaction.dto.response.TransactionResponse;
import com.example.fintech.domain.trasactionRequest.entity.Status;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public interface TransactionService {
    // 결제 시도
    void creatTransaction(TransactionRequestDTO request);

    // 승인된 결제인지 검증
    boolean checkTransRequest(TransactionRequestDTO request);

    // 제한된 소비인지 검증
    void checkConstraint(TransactionRequestDTO request);

    // time limit
    boolean checkTimeLimit(Long userId, LocalDateTime timestamp);

    // amount limit
    boolean checkAmountLimit(Long userId, int amount);

    // daily limit
    boolean checkDailyLimit(Long userId,int amount);

    // category limit
    boolean checkCategoryLimit(Long userId, int mccCode);
}
