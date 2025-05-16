package com.example.fintech.domain.transaction.service;

import com.example.fintech.domain.account.repository.AccountRepository;
import com.example.fintech.domain.home.exception.HomeErrorCode;
import com.example.fintech.domain.home.exception.HomeException;
import com.example.fintech.domain.transaction.converter.HistoryConverter;
import com.example.fintech.domain.transaction.dto.request.MonthlyReportRequestDTO;
import com.example.fintech.domain.transaction.dto.response.HistoryResponse;
import com.example.fintech.domain.transaction.entity.Transaction;
import com.example.fintech.domain.transaction.repository.TransactionRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{

    private final CustomJwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final HistoryConverter historyConverter;

    // 이용 내역 조회
    @Override
    public HistoryResponse.HistoryResponseDTO getTransHistory(MonthlyReportRequestDTO request, String token) {

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

        String date = request.getDate();
        YearMonth yearMonth = YearMonth.parse(date);

        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        List<Transaction> transactions = transactionRepository.findMonthlyTransactions(accountId, startOfMonth, endOfMonth);

        return historyConverter.toResponseDTO(transactions);
    }
}
