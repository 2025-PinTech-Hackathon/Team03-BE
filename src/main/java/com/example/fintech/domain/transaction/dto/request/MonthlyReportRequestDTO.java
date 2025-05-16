package com.example.fintech.domain.transaction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportRequestDTO {
    private String date; // 형식: "YYYY-MM"
}