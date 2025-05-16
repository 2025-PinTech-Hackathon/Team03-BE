package com.example.fintech.domain.quest.service;

import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;

public interface QuestService {
    QuestResponseDTO createQuest(String token, QuestRequestDTO request);
    void deleteQuest(Long questId, String token);
    QuestResponseDTO endQuest(Long questId, String token);
}
