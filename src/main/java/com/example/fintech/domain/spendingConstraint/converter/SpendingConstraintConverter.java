package com.example.fintech.domain.spendingConstraint.converter;


import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintRequestDTO;
import com.example.fintech.domain.spendingConstraint.dto.response.SpendingConstraintResponseDTO;
import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SpendingConstraintConverter {

    // dto -> entity
    public SpendingConstraint toEntity(SpendingConstraintRequestDTO dto, User user) {

        return SpendingConstraint.builder()
                .user(user)
                .amountLimit(dto.getAmountLimit())
                .timeLimit(dto.getTimeRange())
                .location(dto.getLocation())
                .category(dto.getCategory())
                .dailyLimit(dto.getDailyLimit())
                .build();
    }

    // entity -> dto
    public SpendingConstraintResponseDTO toResponseDTO(SpendingConstraint constraint){
        return SpendingConstraintResponseDTO.builder()
                .category(constraint.getCategory())
                .timeRange(constraint.getTimeLimit())
                .location(constraint.getLocation())
                .dailyLimit(constraint.getDailyLimit())
                .amountLimit(constraint.getAmountLimit())
                .build();
    }

}
