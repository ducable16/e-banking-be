package com.repository;

import com.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.sender.userId = :userId OR t.receiver.userId = :userId")
    List<Transaction> findByUserInvolved(@Param("userId") Integer userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.sender.userId = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year")
    Long getTotalSentByUserInMonth(@Param("userId") Integer userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.receiver.userId = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year")
    Long getTotalReceivedByUserInMonth(@Param("userId") Integer userId, @Param("month") int month, @Param("year") int year);

    @Query(value = """
    SELECT 
        DATE_TRUNC('month', t.created_at) AS month,
        SUM(CASE WHEN t.sender_id = :userId THEN t.amount ELSE 0 END) AS total_sent,
        SUM(CASE WHEN t.receiver_id = :userId THEN t.amount ELSE 0 END) AS total_received
    FROM transactions t
    WHERE 
        (t.sender_id = :userId OR t.receiver_id = :userId)
        AND t.created_at >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '11 months'
    GROUP BY month
    ORDER BY month ASC
    """, nativeQuery = true)
    List<Object[]> getMonthlyStatsLast12Months(@Param("userId") Integer userId);

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.createdAt BETWEEN :startDate AND :endDate
        ORDER BY t.createdAt DESC
    """)
    List<Transaction> findAllByCreatedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Transaction> findBySender_EmailContainingIgnoreCaseAndCreatedAtBetween(
            String email, LocalDateTime start, LocalDateTime end
    );

    List<Transaction> findBySender_AccountContainingAndCreatedAtBetween(
            String account, LocalDateTime start, LocalDateTime end
    );
    List<Transaction> findByReceiver_EmailContainingIgnoreCaseAndCreatedAtBetween(
            String email, LocalDateTime start, LocalDateTime end
    );
    List<Transaction> findByReceiver_AccountContainingAndCreatedAtBetween(
            String account, LocalDateTime start, LocalDateTime end
    );
    @Query("""
    SELECT t FROM Transaction t
    WHERE 
        (t.sender.account LIKE %:keyword% OR t.receiver.account LIKE %:keyword%)
        AND t.createdAt BETWEEN :startDate AND :endDate
    ORDER BY t.createdAt DESC
""")
    List<Transaction> findBySenderOrReceiverAccountContainingAndCreatedAtBetween(
            @Param("keyword") String keyword,
            @Param("startDate") LocalDateTime start,
            @Param("endDate") LocalDateTime end
    );
    @Query("""
    SELECT t FROM Transaction t
    WHERE 
        (LOWER(t.sender.email) LIKE LOWER(CONCAT('%', :keyword, '%')) 
        OR LOWER(t.receiver.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND t.createdAt BETWEEN :startDate AND :endDate
    ORDER BY t.createdAt DESC
""")
    List<Transaction> findBySenderOrReceiverEmailContainingAndCreatedAtBetween(
            @Param("keyword") String keyword,
            @Param("startDate") LocalDateTime start,
            @Param("endDate") LocalDateTime end
    );

    List<Transaction> findTop10ByOrderByCreatedAtDesc();
}
