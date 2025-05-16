package com.example.fintech.domain.transaction.service;

import com.example.fintech.domain.transaction.dto.request.MonthlyReportRequestDTO;
import com.example.fintech.domain.transaction.dto.response.MonthlyReportResponseDTO;

public interface PaymentReportService {
    MonthlyReportResponseDTO generateMonthlyReport(String token, MonthlyReportRequestDTO request);
}