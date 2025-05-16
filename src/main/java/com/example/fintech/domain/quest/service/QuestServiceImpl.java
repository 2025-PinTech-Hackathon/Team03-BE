package com.example.fintech.domain.quest.service;

import com.example.fintech.domain.account.entity.Account;
import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.parentChild.repository.ParentChildRepository;
import com.example.fintech.domain.quest.converter.QuestConverter;
import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.entity.Quest;
import com.example.fintech.domain.quest.entity.Status;
import com.example.fintech.domain.quest.exception.QuestErrorCode;
import com.example.fintech.domain.quest.exception.QuestException;
import com.example.fintech.domain.quest.repository.QuestRepository;
import com.example.fintech.domain.quest.dto.response.QuestListResponseDTO;
import com.example.fintech.domain.transaction.repository.TransactionRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import com.example.fintech.domain.transaction.entity.Transaction;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestServiceImpl implements QuestService {

    private final QuestRepository questRepository;
    private final QuestConverter questConverter;
    private final CustomJwtUtil CustomJwtUtil;
    private final UserRepository userRepository;
    private final ParentChildRepository parentChildRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // 퀘스트 생성
    @Override
    public QuestResponseDTO createQuest(String token, QuestRequestDTO request) {
        Long userId = CustomJwtUtil.getUserId(token);

        User parent = userRepository.findById(userId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.USER_NOT_FOUND));

        List<User> children = parent.getChildren();
        if (children.isEmpty()) {
            throw new QuestException(QuestErrorCode.CHILD_NOT_FOUND);
        }

        User child = children.get(0);
        Long childId = child.getId();

        Quest quest = questConverter.toEntity(request, child);
        questRepository.save(quest);

        return questConverter.toResponse(quest, childId);
    }

    // 퀘스트 삭제
    @Override
    public void deleteQuest(Long questId, String token) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

        questRepository.delete(quest);
    }

    // 퀘스트 종료
    @Override
    public void endQuest(Long questId, String token) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

        quest.setStatus(Status.END);
        questRepository.save(quest);

    }

    // 퀘스트 성공
    @Override
    @Transactional
    public void completeQuest(Long questId, String token) {
        try {
            // 1. 부모 ID 추출
            Long parentId = CustomJwtUtil.getUserId(token);

            // 2. 퀘스트 조회
            Quest quest = questRepository.findById(questId)
                    .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

            // 3. 퀘스트 주인(자녀) 가져오기
            User child = quest.getUser();
            if (child == null) {
                throw new QuestException(QuestErrorCode.CHILD_NOT_FOUND);
            }
            Long childId = child.getId();

            // 4. 자녀의 계좌 조회
            Account childAccount = accountRepository.findByUserId(childId)
                    .orElseThrow(() -> new QuestException(QuestErrorCode.ACCOUNT_NOT_FOUND));

            // 5. Transaction 생성
            Transaction transaction = Transaction.builder()
                    .account(childAccount)
                    .merchantName("퀘스트(" + quest.getTitle() + ")")
                    .amount(quest.getReward())
                    .timestamp(LocalDateTime.now())
                    .categoryId(20L)
                    .build();

            transactionRepository.save(transaction);

            // 6. 퀘스트 상태 SUCCESS로 변경
            quest.setStatus(Status.SUCCESS);
            questRepository.save(quest);

        } catch (Exception e) {
            throw new QuestException(QuestErrorCode.MONEY_TRANSFER_FAILED);
        }
    }

    // 퀘스트 전체 목록 조회
    @Override
    public List<QuestListResponseDTO> getQuests(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new QuestException(QuestErrorCode.TOKEN_MISSING);
        }

        Long userId = CustomJwtUtil.getUserId(token);
        String role = CustomJwtUtil.getRole(token);

        List<Quest> quests;

        if ("CHILD".equals(role)) {
            quests = questRepository.findByUserId(userId);

        } else if ("PARENT".equals(role)) {
            User parent = userRepository.findById(userId)
                    .orElseThrow(() -> new QuestException(QuestErrorCode.PARENT_NOT_FOUND));

            List<Long> childIds = parent.getChildren().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            quests = questRepository.findByUserIdIn(childIds);

        } else {
            throw new QuestException(QuestErrorCode.INVALID_ROLE);
        }

        return quests.stream()
                .map(questConverter::toResponse)
                .collect(Collectors.toList());
    }

    // 퀘스트 상세 조회
    @Override
    @Transactional(readOnly = true)
    public QuestListResponseDTO getQuestDetail(Long questId) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

        return questConverter.toResponse(quest);
    }
}