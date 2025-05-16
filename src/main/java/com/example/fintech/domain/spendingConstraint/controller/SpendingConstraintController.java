package com.example.fintech.domain.spendingConstraint.controller;

import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintRequestDTO;
import com.example.fintech.domain.spendingConstraint.dto.response.SpendingConstraintResponseDTO;
import com.example.fintech.domain.spendingConstraint.service.SpendingConstraintService;
import com.example.fintech.global.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/limits")
@RequiredArgsConstructor
public class SpendingConstraintController {

    private final SpendingConstraintService constraintService;

    @PutMapping("/change")
    public ApiResponse<String> putSpendingLimits(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SpendingConstraintRequestDTO request){

        constraintService.putSpendingLimits(authHeader,request);

        return ApiResponse.onSuccess("성공");
    }

    @GetMapping("/")
    public ApiResponse<SpendingConstraintResponseDTO> getLimits(@RequestHeader("Authorization") String authHeader){
        SpendingConstraintResponseDTO response = constraintService.getSpendingLimits(authHeader);
        return ApiResponse.onSuccess(response);
    }


}
