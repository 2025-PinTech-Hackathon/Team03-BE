package com.example.fintech.domain.spendingConstraint.converter;


import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintsRequestDTO;
import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SpendingConstraintConverter {

    // dto -> entity
    public SpendingConstraint toEntity(SpendingConstraintsRequestDTO dto, User user) {

        return SpendingConstraint.builder()
                .user(user)
                .amountLimit(dto.getAmountLimit())
                .timeLimit(dto.getTimeRange())
                .location(dto.getLocation())
                .category(dto.getCategory())
                .dailyLimit(dto.getDailyLimit())
                .build();
    }

}
