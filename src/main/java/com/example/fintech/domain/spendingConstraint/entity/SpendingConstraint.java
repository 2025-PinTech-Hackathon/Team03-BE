package com.example.fintech.domain.spendingConstraint.entity;

import com.example.fintech.domain.spendingConstraint.converter.StringListJsonConverter;
import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintRequestDTO;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpendingConstraint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amountLimit;

    private int dailyLimit;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "category", columnDefinition = "json")
    private List<String> category;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "time_limit", columnDefinition = "json")
    private List<String> timeLimit;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "location", columnDefinition = "json")
    private List<String> location;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateFrom(SpendingConstraintRequestDTO dto) {
        this.amountLimit = dto.getAmountLimit();
        this.category = dto.getCategory();
        this.dailyLimit = dto.getDailyLimit();
        this.timeLimit = dto.getTimeRange();
        this.updatedAt = LocalDateTime.now();
    }
}
