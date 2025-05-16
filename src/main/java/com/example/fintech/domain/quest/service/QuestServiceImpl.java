package com.example.fintech.domain.quest.service;

import com.example.fintech.domain.account.entity.Account;
import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.merchantCategory.entity.MerchantCategory;
import com.example.fintech.domain.merchantCategory.repository.MerchantCategoryRepository;
import com.example.fintech.domain.parentChild.repository.ParentChildRepository;
import com.example.fintech.domain.quest.converter.QuestConverter;
import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.entity.Quest;
import com.example.fintech.domain.quest.entity.Status;
import com.example.fintech.domain.quest.exception.QuestErrorCode;
import com.example.fintech.domain.quest.exception.QuestException;
import com.example.fintech.domain.quest.repository.QuestRepository;
import com.example.fintech.domain.transaction.repository.TransactionRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import com.example.fintech.domain.transaction.entity.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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
    private final MerchantCategoryRepository merchantCategoryRepository;

    // 퀘스트 생성
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

    // 퀘스트 삭제
    @Override
    public void deleteQuest(Long questId, String token) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

        questRepository.delete(quest);
    }

    // 퀘스트 종료
    @Override
    public QuestResponseDTO endQuest(Long questId, String token) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.QUEST_NOT_FOUND));

        quest.setStatus(Status.END);
        questRepository.save(quest);

        return questConverter.toResponse(quest);
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

            // 3. 부모 유저 조회
            User parent = userRepository.findById(parentId)
                    .orElseThrow(() -> new QuestException(QuestErrorCode.USER_NOT_FOUND));

            // 4. 자녀 가져오기
            List<User> children = parent.getChildren();
            if (children.isEmpty()) {
                throw new QuestException(QuestErrorCode.CHILD_NOT_FOUND);
            }

            User child = children.get(0);
            Long childId = child.getId();

            // 5. 자녀의 계좌 조회
            Account childAccount = accountRepository.findByUserId(childId)
                    .orElseThrow(() -> new QuestException(QuestErrorCode.ACCOUNT_NOT_FOUND));

            // 6. Transaction 생성
            MerchantCategory category = merchantCategoryRepository.findById(20L)
                .orElseThrow(() -> new QuestException(QuestErrorCode.CATEGORY_NOT_FOUND));

            Transaction transaction = Transaction.builder()
                    .account(childAccount)
                    .merchantName("퀘스트(" + quest.getTitle() + ")")
                    .amount(quest.getReward())
                    .timestamp(LocalDateTime.now())
                    .merchantCategory(category)
                    .build();

            transactionRepository.save(transaction);

            // 7. 퀘스트 상태 SUCCESS로 변경
            quest.setStatus(Status.SUCCESS);
            questRepository.save(quest);

        } catch (Exception e) {
            throw new QuestException(QuestErrorCode.MONEY_TRANSFER_FAILED);
        }
    }
}