package com.example.fintech.domain.home.service;

import com.example.fintech.domain.home.dto.response.AiCommentResponseDTO;
import com.example.fintech.domain.home.dto.response.HomeResponseDTO;

public interface HomeService {
    public AiCommentResponseDTO getAiComment(String token);
    HomeResponseDTO getHomeInfo(String token);
}
