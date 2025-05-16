package com.example.fintech.domain.quest.controller;


import com.corundumstudio.socketio.SocketIOServer;

import com.example.fintech.domain.quest.dto.response.QuestListResponseDTO;

import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.service.QuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.global.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    private final SocketIOServer socketIOServer;

    // 퀘스트 생성
    @PostMapping("/create")
    public ApiResponse<QuestResponseDTO> createQuest(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody QuestRequestDTO request
    ) {
        QuestResponseDTO response = questService.createQuest(authHeader, request);

        // 🧩 자녀 ID = 퀘스트 수신 대상
        String childId = response.getChildId().toString(); // 혹은 childId 추출

        // 🧩 소켓으로 퀘스트 push
        socketIOServer.getRoomOperations(childId)
                .sendEvent("quest", response);


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

    // 퀘스트 종료
    @PatchMapping("/{questId}/end")
    public ApiResponse<QuestResponseDTO> endQuest(
            @PathVariable Long questId,
            @RequestHeader("Authorization") String token
    ) {
        questService.endQuest(questId, token);
        return ApiResponse.onSuccess(null);
    }

    // 퀘스트 성공
    @PatchMapping("/{questId}/complete")
    public ApiResponse<Void> completeQuest(@PathVariable Long questId,
                                           @RequestHeader("Authorization") String token) {
        questService.completeQuest(questId, token);
        return ApiResponse.onSuccess(null);
    }

    // 퀘스트 전체 목록 조회
    @GetMapping
    public ApiResponse<List<QuestListResponseDTO>> getQuests(
            @RequestHeader("Authorization") String token
    ) {
        List<QuestListResponseDTO> result = questService.getQuests(token);
        return ApiResponse.onSuccess(result);
    }

    // 퀘스트 상세 조회
    @GetMapping("/{questId}")
    public ApiResponse<QuestListResponseDTO> getQuestDetail(
            @PathVariable Long questId
    ) {
        QuestListResponseDTO result = questService.getQuestDetail(questId);
        return ApiResponse.onSuccess(result);
    }
}