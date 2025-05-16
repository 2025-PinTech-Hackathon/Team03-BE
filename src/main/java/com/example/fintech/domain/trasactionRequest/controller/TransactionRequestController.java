package com.example.fintech.domain.trasactionRequest.controller;


import com.corundumstudio.socketio.SocketIOServer;
import com.example.fintech.domain.trasactionRequest.Converter.TransactionRequestConverter;
import com.example.fintech.domain.trasactionRequest.dto.TransactionApproveRequestDTO;
import com.example.fintech.domain.trasactionRequest.dto.TransactionReqRequestDTO;
import com.example.fintech.domain.trasactionRequest.dto.TransactionReqResponseDTO;
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
    private final SocketIOServer socketIOServer;

    @PostMapping("/attempt")
    public ApiResponse<Void> requestTransaction (
            @RequestHeader("Authorization") String token,
            @RequestBody TransactionReqRequestDTO transactionReqRequestDTO
    ){
        TransactionRequest entity = transactionRequestConverter.toEntity(transactionReqRequestDTO, token);
        transactionRequestService.saveTransaction(entity);

        String childId = entity.getUser().getId().toString();

        TransactionReqResponseDTO responseDTO = transactionRequestConverter.toResponseDTO(entity);




        socketIOServer.getRoomOperations(childId).getClients().stream()
                .filter(client -> "PARENT".equals(client.get("role")))
                .forEach(client -> client.sendEvent("ask-approval", responseDTO));

        return ApiResponse.onSuccess(null);
    }

    @PatchMapping("/approve")
    public ApiResponse<Void> approveTransaction (
            @RequestHeader("Authorization") String token,
            @RequestBody TransactionApproveRequestDTO dto
            ){
        transactionRequestService.approveTransaction(token, dto);
        return ApiResponse.onSuccess(null);


    }
}
