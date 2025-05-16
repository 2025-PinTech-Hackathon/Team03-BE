package com.example.fintech.domain.spendingConstraint.repository;

import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingConstraintRepository extends JpaRepository<SpendingConstraint, Long> {
}
