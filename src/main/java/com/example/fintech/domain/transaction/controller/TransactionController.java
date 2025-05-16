package com.example.fintech.domain.transaction.controller;

import com.example.fintech.domain.transaction.dto.request.TransactionRequestDTO;
import com.example.fintech.domain.transaction.dto.response.TransactionResponse;
import com.example.fintech.domain.transaction.service.TransactionService;
import com.example.fintech.global.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // 결제 시도
    @PostMapping("/payments/attempt")
    public ApiResponse<String> payAttempt(@Valid @RequestBody TransactionRequestDTO request){

        transactionService.creatTransaction(request);
        return ApiResponse.onSuccess("거래 성공");
    }
}
