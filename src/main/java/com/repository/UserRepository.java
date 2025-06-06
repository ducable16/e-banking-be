package com.repository;

import com.entity.User;
import com.enums.AccountStatus;
import com.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(Integer userId);

    Optional<User> findByEmail(String email);

    List<User> findUsersByRole(Role role);

    boolean existsByAccount(String account);

    Optional<User> findByAccount(String account);

    long countByStatus(AccountStatus status);

    @Query("SELECT SUM(u.balance) FROM User u")
    long sumAllBalances();
}