package com.repository;

import com.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
