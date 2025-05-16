package com.example.fintech.domain.home.converter;


import com.example.fintech.domain.home.dto.response.HomeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class HomeConverter {

    public HomeResponseDTO toResponse(long inProgressCount, double leftMoney, int todayRemain) {
        return HomeResponseDTO.builder()
                .inProgressCount((int) inProgressCount)
                .leftMoney(leftMoney)
                .todayTotalSpend(todayRemain)
                .build();
    }
}
