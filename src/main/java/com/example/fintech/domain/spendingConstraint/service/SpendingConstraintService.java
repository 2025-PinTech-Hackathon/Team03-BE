package com.example.fintech.domain.spendingConstraint.service;

import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintRequestDTO;
import com.example.fintech.domain.spendingConstraint.dto.response.SpendingConstraintResponseDTO;

public interface SpendingConstraintService {
    //소비 제한 생성 & 수정
    void putSpendingLimits(String authHeader, SpendingConstraintRequestDTO request);
    // 소비 제한 조회
    SpendingConstraintResponseDTO getSpendingLimits(String authHeader);
}
