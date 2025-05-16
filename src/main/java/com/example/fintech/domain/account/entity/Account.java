package com.example.fintech.domain.account.entity;

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
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String bankCode;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
