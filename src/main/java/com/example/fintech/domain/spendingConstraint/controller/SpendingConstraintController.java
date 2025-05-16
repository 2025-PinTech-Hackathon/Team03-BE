package com.example.fintech.domain.spendingConstraint.controller;

import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintsRequestDTO;
import com.example.fintech.domain.spendingConstraint.service.SpendingConstraintService;
import com.example.fintech.global.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/limits")
@RequiredArgsConstructor
public class SpendingConstraintController {

    private final SpendingConstraintService spendingConstraintService;

    @PutMapping("/change")
    public ApiResponse<String> putSpendingLimits(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SpendingConstraintsRequestDTO request){

        spendingConstraintService.putSpendingLimits(authHeader,request);

        return ApiResponse.onSuccess("성공");
    }
}
