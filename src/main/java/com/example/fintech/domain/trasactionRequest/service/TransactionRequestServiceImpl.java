package com.example.fintech.domain.trasactionRequest.service;

import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.spendingConstraint.repository.SpendingConstraintRepository;
import com.example.fintech.domain.transaction.exception.TransactionErrorCode;
import com.example.fintech.domain.transaction.exception.TransactionException;
import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;
import com.example.fintech.domain.trasactionRequest.repository.TransactionRequestRepository;
import com.example.fintech.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionRequestServiceImpl implements TransactionRequestService {

    private final TransactionRequestRepository transactionRequestRepository;
    private final UserRepository userRepository;
    private final SpendingConstraintRepository constraintRepository;

    @Transactional
    public void saveTransaction(TransactionRequest entity) {
        Long childId = entity.getUser().getId(); // 또는 entity.getUserId();

        SpendingConstraint constraint = constraintRepository.findByUserId(childId)
                .orElseThrow(() -> new TransactionException(TransactionErrorCode.USER_NOT_FOUND));

        entity.setSpendingConstraint(constraint);



        transactionRequestRepository.save(entity);
    }
}
