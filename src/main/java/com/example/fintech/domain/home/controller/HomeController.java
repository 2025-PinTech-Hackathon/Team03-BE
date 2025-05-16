package com.example.fintech.domain.home.controller;

import com.example.fintech.domain.home.dto.response.AiCommentResponseDTO;
import com.example.fintech.domain.home.service.HomeService;
import com.example.fintech.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/ai-comment")
    public ApiResponse<AiCommentResponseDTO> getAiComment(@RequestHeader("Authorization") String token) {
        AiCommentResponseDTO responseDTO = homeService.getAiComment(token);
        return ApiResponse.onSuccess(responseDTO);
    }
}