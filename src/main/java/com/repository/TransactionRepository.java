package com.repository;

import com.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.sender.userId = :userId OR t.receiver.userId = :userId")
    List<Transaction> findByUserInvolved(@Param("userId") Integer userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.sender.userId = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year")
    Long getTotalSentByUserInMonth(@Param("userId") Integer userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.receiver.userId = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year")
    Long getTotalReceivedByUserInMonth(@Param("userId") Integer userId, @Param("month") int month, @Param("year") int year);

}
