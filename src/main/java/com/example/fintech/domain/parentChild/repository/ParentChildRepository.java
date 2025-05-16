package com.example.fintech.domain.parentChild.repository;

import com.example.fintech.domain.parentChild.entity.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentChildRepository extends JpaRepository<ParentChild, Long> {
    Optional<ParentChild> findByParentId(Long userId);
}
