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
import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;
import com.example.fintech.domain.trasactionRequest.repository.TransactionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

        // transaction request 검사
        boolean approved = checkTransRequest(request);

        // constraint 검사
        if (approved) {
            // 승인된 경우 PAID로 바꾸고 결제 처리
            updatePaid(request.getUserId(), request.getAmount(), request.getMerchantName());
        } else{
            // 승인 안 된 경우에만 제약조건 검사
            checkConstraint(request);
        }

        // 결제 성공
        Transaction transaction = transactionConverter.toEntity(request, account, categoryId);
        transactionRepository.save(transaction);

    }

    // constraint 검증
    @Override
    public void checkConstraint(TransactionRequestDTO request) {

        Long userId = request.getUserId();
        int amount = request.getAmount();
        int mccCode = request.getMccCode();
        LocalDateTime timestamp = request.getTimestamp();

        checkTimeLimit(userId, timestamp);
        checkAmountLimit(userId, amount);
        checkDailyLimit(userId, amount);
        checkCategoryLimit(request);
    }

    // time limit 검증
    @Override
    public boolean checkTimeLimit(Long userId, LocalDateTime timestamp) {

        Optional<SpendingConstraint> optionalConstraint = constraintRepository.findByUserId(userId);
        if (optionalConstraint.isEmpty()) return true; //제헌 없음

        List<String> timeLimit = optionalConstraint.get().getTimeLimit();
        if (timeLimit == null || timeLimit.size() != 2) return true;

        LocalTime start = LocalTime.parse(timeLimit.get(0)); // ex: "09:00"
        LocalTime end = LocalTime.parse(timeLimit.get(1));   // ex: "20:00"
        LocalTime now = timestamp.toLocalTime();             // ex: 15:30

        if (now.isBefore(start) || now.isAfter(end)) {
            TransactionResponse.failureTimeLimitDTO failureDTO = TransactionResponse.failureTimeLimitDTO.builder()
                    .allowedTimeRange(timeLimit)
                    .attemptedTime(now.toString())
                    .build();

            throw new TransactionException(TransactionErrorCode.TIME_LIMIT, failureDTO);
        }

        return true;
    }

    @Override
    public boolean checkAmountLimit(Long userId, int amount) {
        return false;
    }

    @Override
    public boolean checkDailyLimit(Long userId, int amount) {
        return false;
    }

    // 카테고리 검증
    @Override
    public boolean checkCategoryLimit(TransactionRequestDTO request) {
        Long userId = request.getUserId();
        int mccCode = request.getMccCode();

        Optional<SpendingConstraint> optionalConstraint = constraintRepository.findByUserId(userId);
        if (optionalConstraint.isEmpty()) return true;

        List<String> blockedCategories = optionalConstraint.get().getCategory();

        if (blockedCategories == null || blockedCategories.isEmpty()) return true; // 제한 없음

        // 제한된 업종 이름에 해당하는 merchantCategory 코드 리스트 조회
        List<Integer> blockedMccCodes = categoryRepository.findAllByNameIn(blockedCategories)
                .stream()
                .map(MerchantCategory::getCode)
                .toList();

        // 요청 mccCode가 제한된 코드에 있으면 예외 발생
        if (blockedMccCodes.contains(mccCode)) {
            TransactionResponse.failureCategoryDTO failureDTO = transactionConverter.toFailureCategoryDTO(request, "업종 제한");
            throw new TransactionException(TransactionErrorCode.CATEGORY_LIMIT,failureDTO);
        }

        return true;
    }

    // transaction request 검증
    @Override
    public boolean checkTransRequest(TransactionRequestDTO request) {

        Long userId = request.getUserId();
        int amount = request.getAmount();
        String merchantName = request.getMerchantName();

        return transRequestRepository.findLatestApprovedRequest(userId, amount, merchantName)
                .isPresent();
    }

    @Transactional
    public void updatePaid(Long userId, int amount, String merchantName) {
        Optional<TransactionRequest> optionalRequest = transRequestRepository
                .findTopByUserIdAndAmountAndMerchantNameAndStatusOrderByCreatedAtDesc(
                        userId,
                        amount,
                        merchantName,
                        Status.APPROVE
                );

        if (optionalRequest.isEmpty()) {
            throw new TransactionException(TransactionErrorCode.REQUEST_NOT_APPROVED);
        }

        TransactionRequest transactionRequest = optionalRequest.get();
        transactionRequest.setStatus(Status.PAID);
        transRequestRepository.save(transactionRequest);
    }



}
