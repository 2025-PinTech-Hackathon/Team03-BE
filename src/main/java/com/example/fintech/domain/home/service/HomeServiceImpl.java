package com.example.fintech.domain.home.service;

import com.example.fintech.domain.account.entity.Account;
import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.home.dto.request.AiRequestDTO;
import com.example.fintech.domain.home.dto.request.AiTransactionDTO;
import com.example.fintech.domain.home.dto.response.AiCommentResponseDTO;
import com.example.fintech.domain.home.exception.HomeErrorCode;
import com.example.fintech.domain.home.exception.HomeException;
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
    private final CustomJwtUtil jwtUtil;
    private final RestTemplate restTemplate;
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
}
