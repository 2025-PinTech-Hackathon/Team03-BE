package com.example.fintech.domain.quest.service;

import com.example.fintech.domain.parentChild.repository.ParentChildRepository;
import com.example.fintech.domain.quest.converter.QuestConverter;
import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.entity.Quest;
import com.example.fintech.domain.quest.exception.QuestErrorCode;
import com.example.fintech.domain.quest.exception.QuestException;
import com.example.fintech.domain.quest.repository.QuestRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class QuestServiceImpl implements QuestService {

    private final QuestRepository questRepository;
    private final QuestConverter questConverter;
    private final CustomJwtUtil CustomJwtUtil;
    private final UserRepository userRepository;
    private final ParentChildRepository parentChildRepository;

    @Override
    public QuestResponseDTO createQuest(String token, QuestRequestDTO request) {
        Long userId = CustomJwtUtil.getUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.USER_NOT_FOUND));

        if (!request.getDeadline().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
            throw new QuestException(QuestErrorCode.INVALID_DATE_FORMAT);
        }

        Long childId = parentChildRepository.findByParentId(userId)
                .map(parentChild -> parentChild.getChild().getId())
                .orElseThrow(() -> new QuestException(QuestErrorCode.CHILD_NOT_FOUND));


        Quest quest = questConverter.toEntity(request, user);
        questRepository.save(quest);

        return questConverter.toResponse(quest, childId);
    }

    @Override
    public void deleteQuest(Long questId, String token) {
        Long userId = CustomJwtUtil.getUserId(token);

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

        questRepository.delete(quest);
    }


}