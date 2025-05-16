package com.example.fintech.domain.home.service;

import com.example.fintech.domain.account.entity.Account;
import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.home.converter.HomeConverter;
import com.example.fintech.domain.home.dto.request.AiRequestDTO;
import com.example.fintech.domain.home.dto.request.AiTransactionDTO;
import com.example.fintech.domain.home.dto.response.AiCommentResponseDTO;
import com.example.fintech.domain.home.dto.response.HomeResponseDTO;
import com.example.fintech.domain.home.exception.HomeErrorCode;
import com.example.fintech.domain.home.exception.HomeException;
import com.example.fintech.domain.quest.entity.Status;
import com.example.fintech.domain.quest.repository.QuestRepository;
import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.spendingConstraint.exception.SpendingConstraintErrorCode;
import com.example.fintech.domain.spendingConstraint.exception.SpendingConstraintException;
import com.example.fintech.domain.spendingConstraint.repository.SpendingConstraintRepository;
import com.example.fintech.domain.transaction.entity.Transaction;
import com.example.fintech.domain.transaction.repository.TransactionRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final QuestRepository questRepository;
    private final SpendingConstraintRepository constraintRepository;
    private final CustomJwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final HomeConverter homeConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${ai.server-url}")
    private String aiBaseUrl;

    @Override
    public AiCommentResponseDTO getAiComment(String token) {
        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        Long accountId;

        if ("PARENT".equals(role)) {
            User parent = userRepository.findById(userId)
                    .orElseThrow(() -> new HomeException(HomeErrorCode.USER_NOT_FOUND));

            User firstChild = parent.getChildren().stream()
                    .findFirst()
                    .orElseThrow(() -> new HomeException(HomeErrorCode.CHILD_ACCOUNT_NOT_FOUND));

            accountId = accountRepository.findByUserId(firstChild.getId())
                    .orElseThrow(() -> new HomeException(HomeErrorCode.CHILD_ACCOUNT_NOT_FOUND))
                    .getId();
        } else {
            accountId = accountRepository.findByUserId(userId)
                    .orElseThrow(() -> new HomeException(HomeErrorCode.ACCOUNT_NOT_FOUND))
                    .getId();
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Transaction> todayNegativeTransactions = transactionRepository
                .findTodayExpenses(accountId, startOfDay, endOfDay);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<AiTransactionDTO> transactionDTOs = todayNegativeTransactions.stream()
                .map(tx -> new AiTransactionDTO(
                        tx.getMerchantName(),
                        tx.getAmount(),
                        tx.getTimestamp().format(formatter)
                ))
                .collect(Collectors.toList());

        AiRequestDTO requestDTO = new AiRequestDTO(userId, role, transactionDTOs);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestDTO);
            System.out.println("[DEBUG] AI 요청 Body JSON: " + requestBodyJson);
        } catch (Exception e) {
            System.out.println("[ERROR] JSON 직렬화 실패: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String summaryUrl = aiBaseUrl + "/summary";
            ResponseEntity<Map> response = restTemplate.postForEntity(summaryUrl, request, Map.class);
            String summary = ((Map<?, ?>) response.getBody()).get("summary").toString();
            summary = summary.replaceAll("^\"|\"$", "");
            return new AiCommentResponseDTO(summary);
        } catch (Exception e) {
            System.out.println("[ERROR] AI 서버 요청 실패: " + e.getMessage());
            e.printStackTrace();
            throw new HomeException(HomeErrorCode.AI_REQUEST_FAIL);
        }
    }

    // 홈 정보 조회
    @Override
    public HomeResponseDTO getHomeInfo(String token) {

        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        User child;
        if ("PARENT".equals(role)) {
            User parent = userRepository.findById(userId)
                    .orElseThrow(() -> new HomeException(HomeErrorCode.USER_NOT_FOUND));

            child = parent.getChildren().stream()
                    .findFirst()
                    .orElseThrow(() -> new HomeException(HomeErrorCode.CHILD_NOT_FOUND));

        }else{
            child = userRepository.findById(userId)
                    .orElseThrow(() -> new HomeException(HomeErrorCode.CHILD_NOT_FOUND));
        }

        // 1. 진행 중인 쿼스트 개수 조회
        long inProgressCount = questRepository.countByUserIdAndStatus(child.getId(), Status.CHALLENGING);

        // 2. 계좌 잔액 조회
        double leftMoney = accountRepository.findByUserId(child.getId())
                .map(Account::getBalance)
                .orElse(0.0);

        // 3. 오늘의 남은 금액 조회
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        SpendingConstraint constraint = constraintRepository.findByUserId(child.getId())
                .orElseThrow(() -> new SpendingConstraintException(SpendingConstraintErrorCode.CONSTRAINT_NOT_FOUND));

        Long accountId = accountRepository.findByUserId(child.getId())
                .orElseThrow(() -> new HomeException(HomeErrorCode.ACCOUNT_NOT_FOUND))
                .getId();

        int dailyLimit = constraint.getDailyLimit();

        System.out.println("dailyLimit = " + dailyLimit);
        System.out.println("today = " + today);
        System.out.println("startOfDay = " + startOfDay);
        System.out.println("endOfDay = " + endOfDay);

        int spent = transactionRepository.sumTodayExpenses(accountId, startOfDay, endOfDay);
        System.out.println("spent = " + spent);
        int todayRemain = Math.max(0, dailyLimit - Math.abs(spent));

        return homeConverter.toResponse(
                inProgressCount,
                leftMoney,
                todayRemain
        );
    }
}
