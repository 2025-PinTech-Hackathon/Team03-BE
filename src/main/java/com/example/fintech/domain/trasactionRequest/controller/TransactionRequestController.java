package com.example.fintech.domain.trasactionRequest.controller;


import com.example.fintech.domain.trasactionRequest.Converter.TransactionRequestConverter;
import com.example.fintech.domain.trasactionRequest.dto.TransactionRequestDTO;
import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;
import com.example.fintech.domain.trasactionRequest.service.TransactionRequestService;
import com.example.fintech.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class TransactionRequestController {

    private final TransactionRequestConverter transactionRequestConverter;
    private final TransactionRequestService transactionRequestService;

    @PostMapping("/attempt")
    public ApiResponse<Void> requestTransaction (
            @RequestHeader("Authorization") String token,
            @RequestBody TransactionRequestDTO transactionRequestDTO
    ){
        TransactionRequest entity = transactionRequestConverter.toEntity(transactionRequestDTO, token);
        transactionRequestService.saveTransaction(entity);
        return ApiResponse.onSuccess(null);
    }
}
