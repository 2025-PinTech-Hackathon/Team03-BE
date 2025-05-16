package com.example.fintech.domain.home.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiTransactionDTO {
    @JsonProperty("merchant_name")
    private String merchant_name;
    private int amount;
    private String timestamp;
}