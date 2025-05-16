package com.example.fintech.domain.merchantCategory.repository;

import com.example.fintech.domain.merchantCategory.entity.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {
}
