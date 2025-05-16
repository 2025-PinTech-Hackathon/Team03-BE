package com.example.fintech.domain.spendingConstraint.repository;


import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SpendingConstraintRepository extends JpaRepository<SpendingConstraint, Long> {
    Optional<SpendingConstraint> findByUserId(Long userId);
}
