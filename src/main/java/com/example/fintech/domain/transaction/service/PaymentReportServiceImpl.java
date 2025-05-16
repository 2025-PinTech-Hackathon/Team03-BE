package com.example.fintech.domain.transaction.service;

import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.home.dto.request.AiTransactionDTO;
import com.example.fintech.domain.home.exception.HomeErrorCode;
import com.example.fintech.domain.home.exception.HomeException;
import com.example.fintech.domain.home.dto.request.AiRequestDTO;
import com.example.fintech.domain.transaction.dto.response.HistoryResponse;
import com.example.fintech.domain.transaction.entity.Transaction;
import com.example.fintech.domain.transaction.dto.request.MonthlyReportRequestDTO;
import com.example.fintech.domain.transaction.dto.response.MonthlyReportResponseDTO;
import com.example.fintech.domain.transaction.repository.TransactionRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentReportServiceImpl implements PaymentReportService {

    private final CustomJwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;
    private final HistoryService historyService;

    @Value("${ai.server-url}")
    private String aiBaseUrl;

    @Override
    public MonthlyReportResponseDTO generateMonthlyReport(String token, MonthlyReportRequestDTO request) {
        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        // 1. 날짜 범위 계산
        YearMonth ym = YearMonth.parse(request.getDate());
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);

        // 2. accountId 조회
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

        // 3. 거래 내역 조회
        List<Transaction> transactions = transactionRepository
                .findMonthlyExpenses(accountId, start, end);

        // 4. AI 요청 DTO 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<AiTransactionDTO> aiTransactions = transactions.stream()
                .map(tx -> new AiTransactionDTO(
                        tx.getMerchantName(),
                        tx.getAmount(),
                        tx.getTimestamp().format(formatter)
                ))
                .collect(Collectors.toList());

        AiRequestDTO aiRequest = new AiRequestDTO(userId, role, aiTransactions);

        // 5. AI 서버 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequestDTO> httpRequest = new HttpEntity<>(aiRequest, headers);
        String url = aiBaseUrl + "/monthly-report";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, httpRequest, Map.class);
            Map<String, Object> body = response.getBody();

            // 이용내역 호출
            HistoryResponse.HistoryResponseDTO result = historyService.getTransHistory(request, token);

            return MonthlyReportResponseDTO.builder()
                    .summary((String) body.get("summary"))
                    .shopping((Integer) body.get("shopping"))
                    .food((Integer) body.get("food"))
                    .culture((Integer) body.get("culture"))
                    .etc((Integer) body.get("etc"))
                    .spending(result.getHistory())
                    .build();
        } catch (Exception e) {
            throw new HomeException(HomeErrorCode.AI_REQUEST_FAIL);
        }
    }
}