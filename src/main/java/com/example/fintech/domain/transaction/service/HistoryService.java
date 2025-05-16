package com.example.fintech.domain.transaction.service;

import com.example.fintech.domain.transaction.dto.request.MonthlyReportRequestDTO;
import com.example.fintech.domain.transaction.dto.response.HistoryResponse;

public interface HistoryService {

    // 이용내역 조회
    HistoryResponse.HistoryResponseDTO getTransHistory(MonthlyReportRequestDTO request, String token);
}
