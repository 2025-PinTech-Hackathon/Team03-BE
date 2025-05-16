package com.example.fintech.domain.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MonthlyReportResponseDTO {
    private String summary;
    private int shopping;
    private int food;
    private int culture;
    private int etc;

    private List<HistoryResponse.TransactionDTO> spending;
}
