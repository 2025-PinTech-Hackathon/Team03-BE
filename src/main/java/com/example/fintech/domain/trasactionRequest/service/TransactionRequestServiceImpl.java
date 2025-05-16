package com.example.fintech.domain.trasactionRequest.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.fintech.domain.parentChild.repository.ParentChildRepository;
import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.spendingConstraint.repository.SpendingConstraintRepository;
import com.example.fintech.domain.transaction.exception.TransactionErrorCode;
import com.example.fintech.domain.transaction.exception.TransactionException;
import com.example.fintech.domain.trasactionRequest.dto.TransactionApproveRequestDTO;
import com.example.fintech.domain.trasactionRequest.dto.TransactionStatusUpdateDTO;
import com.example.fintech.domain.trasactionRequest.entity.Status;
import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;
import com.example.fintech.domain.trasactionRequest.exception.TransactionRequestErrorCode;
import com.example.fintech.domain.trasactionRequest.exception.TransactionRequestException;
import com.example.fintech.domain.trasactionRequest.repository.TransactionRequestRepository;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionRequestServiceImpl implements TransactionRequestService {

    private final TransactionRequestRepository transactionRequestRepository;
    private final SocketIOServer socketIOServer;
    private final SpendingConstraintRepository constraintRepository;
    private final CustomJwtUtil CustomJwtUtil;
    private final ParentChildRepository parentChildRepository;

    @Transactional
    public void saveTransaction(TransactionRequest entity) {
        Long childId = entity.getUser().getId(); // 또는 entity.getUserId();

        SpendingConstraint constraint = constraintRepository.findByUserId(childId)
                .orElseThrow(() -> new TransactionException(TransactionErrorCode.USER_NOT_FOUND));

        entity.setSpendingConstraint(constraint);



        transactionRequestRepository.save(entity);
    }

    @Transactional
    public void approveTransaction(String token, TransactionApproveRequestDTO dto) {
        Long parentId = CustomJwtUtil.getUserId(token);

        // 1. 부모 ID로 자식 ID 찾기
        Long childId = parentChildRepository.findByParentId(parentId)
                .orElseThrow(() -> new TransactionException(TransactionErrorCode.USER_NOT_FOUND))
                .getChild().getId();

        // 2. 자식 ID + timestamp 일치하는 거래 찾기
        TransactionRequest entity = transactionRequestRepository
                .findByUserIdAndTimestamp(childId, dto.getTimestamp())
                .orElseThrow(() -> new TransactionRequestException(TransactionRequestErrorCode.TRANSACTION_NOT_FOUND));

        // 3. status 업데이트
        entity.setStatus(Status.valueOf(dto.getStatus()));
        String roomId = childId.toString();
        TransactionStatusUpdateDTO response = new TransactionStatusUpdateDTO(dto.getStatus());

        socketIOServer.getRoomOperations(roomId).getClients().stream()
                .filter(client -> "CHILD".equals(client.get("role")))
                .forEach(client -> client.sendEvent("ask-result", response));
    }
}
