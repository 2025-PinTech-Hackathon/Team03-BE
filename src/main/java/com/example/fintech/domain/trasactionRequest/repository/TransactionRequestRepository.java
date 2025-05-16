package com.example.fintech.domain.trasactionRequest.repository;

import com.example.fintech.domain.trasactionRequest.entity.Status;
import com.example.fintech.domain.trasactionRequest.entity.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {

    @Query("SELECT tr.status FROM TransactionRequest tr " +
            "WHERE tr.user.id = :userId AND tr.amount = :amount AND tr.merchantName = :merchantName " +
            "ORDER BY tr.createdAt DESC")
    Optional<Status> findApprovedRequest(
            @Param("userId") Long userId,
            @Param("amount") int amount,
            @Param("merchantName") String merchantName
    );

}
