package com.example.fintech.domain.transaction.service;

import com.example.fintech.domain.account.entity.Account;
import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.merchantCategory.entity.MerchantCategory;
import com.example.fintech.domain.merchantCategory.repository.MerchantCategoryRepository;
import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.spendingConstraint.repository.SpendingConstraintRepository;
import com.example.fintech.domain.transaction.converter.TransactionConverter;
import com.example.fintech.domain.transaction.dto.request.TransactionRequestDTO;
import com.example.fintech.domain.transaction.dto.response.TransactionResponse;
import com.example.fintech.domain.transaction.entity.Transaction;
import com.example.fintech.domain.transaction.exception.TransactionErrorCode;
import com.example.fintech.domain.transaction.exception.TransactionException;
import com.example.fintech.domain.transaction.repository.TransactionRepository;
import com.example.fintech.domain.trasactionRequest.entity.Status;
import com.example.fintech.domain.trasactionRequest.repository.TransactionRequestRepository;
import com.example.fintech.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final TransactionRequestRepository transRequestRepository;
    private final SpendingConstraintRepository constraintRepository;
    private final AccountRepository accountRepository;
    private final MerchantCategoryRepository categoryRepository;
    private final TransactionConverter transactionConverter;


    // 결제 시도
    @Transactional
    @Override
    public void creatTransaction(TransactionRequestDTO request) {

        Account account = accountRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new TransactionException(TransactionErrorCode.USER_NOT_FOUND));


        Long categoryId = categoryRepository.findByCode(request.getMccCode())
                .map(MerchantCategory::getId)
                .orElse(null);



        // constraint 검사 -> 통과
        // transaction request 검사 -> 통과

        // 결제가능
        // converter 호출

        // repository 호출해서 저장
        Transaction transaction = transactionConverter.toEntity(request, account, categoryId);
        transactionRepository.save(transaction);

    }

    // constraint 검증
    @Override
    public void checkConstraint(TransactionRequestDTO request) {

        Long userId = request.getUserId();
        int amount = request.getAmount();
        String merchantName = request.getMerchantName();
        int mccCode = request.getMccCode();
        LocalDateTime timestamp = request.getTimestamp();

        checkTimeLimit(userId, timestamp);
        checkAmountLimit(userId, amount);
        checkDailyLimit(userId, amount);
        checkCategoryLimit(userId, mccCode);
    }

    @Override
    public boolean checkTimeLimit(Long userId, LocalDateTime timestamp) {
        return false;
    }

    @Override
    public boolean checkAmountLimit(Long userId, int amount) {
        return false;
    }

    @Override
    public boolean checkDailyLimit(Long userId, int amount) {
        return false;
    }

    @Override
    public boolean checkCategoryLimit(Long userId, int mccCode) {
        return false;
    }

    // transaction request 검증
    @Override
    public Status checkTransRequest(Long userId, int amount, String merchantName) {

        Status status = transRequestRepository
                .findApprovedRequest(userId, amount, merchantName)
                .orElse(null);  // 없으면 null 반환

        return status;
    }
}
