package com.example.fintech.domain.transaction.repository;


import com.example.fintech.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
