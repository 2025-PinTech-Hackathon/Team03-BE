package com.example.fintech.domain.transaction.controller;

import com.example.fintech.domain.transaction.dto.request.MonthlyReportRequestDTO;
import com.example.fintech.domain.transaction.dto.request.TransactionRequestDTO;
import com.example.fintech.domain.transaction.dto.response.MonthlyReportResponseDTO;
import com.example.fintech.domain.transaction.service.PaymentReportService;
import com.example.fintech.domain.transaction.service.TransactionService;
import com.example.fintech.global.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final PaymentReportService paymentReportService;

    // 결제 시도
    @PostMapping
    public ApiResponse<String> payAttempt(@Valid @RequestBody TransactionRequestDTO request){

        transactionService.creatTransaction(request);
        return ApiResponse.onSuccess("거래 성공");
    }

    // 소비 내역 조회
    @PostMapping("/report")
    public ApiResponse<MonthlyReportResponseDTO> getMonthlyReport(
            @RequestHeader("Authorization") String token,
            @RequestBody MonthlyReportRequestDTO request
    ) {
        MonthlyReportResponseDTO result = paymentReportService.generateMonthlyReport(token, request);
        return ApiResponse.onSuccess(result);
    }
}
