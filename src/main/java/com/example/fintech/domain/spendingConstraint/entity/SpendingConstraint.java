package com.example.fintech.domain.spendingConstraint.entity;

import com.example.fintech.domain.trasactionRequest.entity.Status;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(columnDefinition = "json")
    private String category;

    @Column(columnDefinition = "json")
    private String timeLimit;

    @Column(columnDefinition = "json")
    private String location;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
