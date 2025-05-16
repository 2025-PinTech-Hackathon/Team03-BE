package com.example.fintech.domain.quest.controller;

import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.service.QuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.global.ApiResponse;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;
    private final SimpMessagingTemplate messagingTemplate;
    // 퀘스트 생성
    @PostMapping("/create")
    public ApiResponse<QuestResponseDTO> createQuest(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody QuestRequestDTO request
    ) {
        QuestResponseDTO response = questService.createQuest(authHeader, request);



        messagingTemplate.convertAndSend(
                "/topic/child/" + response.getChildId(),
                response
        );

        System.out.println("[WebSocket] 메시지 전송 경로: /topic/child/" + response.getChildId());
        System.out.println("[WebSocket] 메시지 내용: " + response); // toString 확인

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
}