package com.example.fintech.domain.trasactionRequest.Converter;

import com.example.fintech.domain.transaction.exception.TransactionErrorCode;
import com.example.fintech.domain.transaction.exception.TransactionException;
import com.example.fintech.domain.trasactionRequest.dto.TransactionReqRequestDTO;
import com.example.fintech.domain.trasactionRequest.dto.TransactionReqResponseDTO;
import com.example.fintech.domain.trasactionRequest.entity.Status;
import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRequestConverter {

    private final CustomJwtUtil jwtUtil;
    private final UserRepository userRepository;

    public TransactionRequest toEntity(TransactionReqRequestDTO dto, String token) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TransactionException(TransactionErrorCode.USER_NOT_FOUND));
        return TransactionRequest.builder()
                .merchantName(dto.getMerchantName())
                .amount(dto.getAmount())
                .timestamp(dto.getTimestamp())
                .status(Status.valueOf(dto.getStatus()))
                .reason(dto.getReason())
                .user(user)
                .build();
    }

    public TransactionReqResponseDTO toResponseDTO(TransactionRequest entity) {
        TransactionReqResponseDTO dto = new TransactionReqResponseDTO();
        dto.setReason(entity.getReason());
        dto.setMerchantName(entity.getMerchantName());
        dto.setAmount(entity.getAmount());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }



}