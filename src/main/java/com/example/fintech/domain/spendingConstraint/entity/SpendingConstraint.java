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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
