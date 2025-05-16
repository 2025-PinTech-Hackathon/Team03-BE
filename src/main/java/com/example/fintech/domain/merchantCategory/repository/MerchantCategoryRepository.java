package com.example.fintech.domain.merchantCategory.repository;

import com.example.fintech.domain.merchantCategory.entity.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {
    Optional<MerchantCategory> findByCode(int code);
    List<MerchantCategory> findAllByNameIn(List<String> names);
}
