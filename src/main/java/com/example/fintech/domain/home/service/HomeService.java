package com.example.fintech.domain.home.service;

import com.example.fintech.domain.home.dto.response.AiCommentResponseDTO;

public interface HomeService {
    public AiCommentResponseDTO getAiComment(String token);
}
