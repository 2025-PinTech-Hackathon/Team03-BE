package com.example.fintech.domain.home.controller;

import com.example.fintech.domain.home.dto.response.AiCommentResponseDTO;
import com.example.fintech.domain.home.dto.response.HomeResponseDTO;
import com.example.fintech.domain.home.service.HomeService;
import com.example.fintech.global.ApiResponse;
import lombok.Getter;
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
    public ApiResponse<String> getAiComment(@RequestHeader("Authorization") String token) {
        AiCommentResponseDTO responseDTO = homeService.getAiComment(token);
        return ApiResponse.onSuccess(responseDTO.getSummary());
    }

    // 홈 정보 조회
    @GetMapping("/")
    public ApiResponse<HomeResponseDTO> getHome(@RequestHeader("Authorization") String token){
        HomeResponseDTO responseDTO = homeService.getHomeInfo(token);
        return ApiResponse.onSuccess(responseDTO);
    }
}