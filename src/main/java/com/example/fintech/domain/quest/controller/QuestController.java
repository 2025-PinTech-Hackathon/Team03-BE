package com.example.fintech.domain.quest.controller;

import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.service.QuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.global.ApiResponse;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    // 퀘스트 생성
    @PostMapping("/create")
    public ApiResponse<QuestResponseDTO> createQuest(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody QuestRequestDTO request
    ) {
        QuestResponseDTO response = questService.createQuest(authHeader, request);
        return ApiResponse.onSuccess(response);
    }

    // 퀘스트 삭제
    @DeleteMapping("/{questId}/delete")
    public ApiResponse<Void> deleteQuest(
            @PathVariable Long questId,
            @RequestHeader("Authorization") String token
    ) {
        questService.deleteQuest(questId, token);
        return ApiResponse.onSuccess(null);
    }
}