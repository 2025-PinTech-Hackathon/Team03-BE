package com.example.fintech.domain.spendingConstraint.service;

import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintsRequestDTO;

public interface SpendingConstraintService {
    //소비 제한 생성 & 수정
    void putSpendingLimits(String authHeader, SpendingConstraintsRequestDTO request);
}
