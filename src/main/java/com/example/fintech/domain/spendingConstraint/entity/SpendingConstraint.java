package com.example.fintech.domain.spendingConstraint.entity;

import com.example.fintech.domain.spendingConstraint.converter.StringListJsonConverter;
import com.example.fintech.domain.trasactionRequest.entity.Status;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String amountLimit;

    private String dailyLimit;

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
}
