package com.example.fintech.domain.quest.service;

import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestListResponseDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;

import java.util.List;

public interface QuestService {
    QuestResponseDTO createQuest(String token, QuestRequestDTO request);
    void deleteQuest(Long questId, String token);
    void endQuest(Long questId, String token);
    void completeQuest(Long questId, String token);
    List<QuestListResponseDTO> getQuests(String token);
    QuestListResponseDTO getQuestDetail(Long questId);

}
