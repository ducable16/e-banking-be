package com.repository;

import com.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSessionRepository extends JpaRepository<UserSession, Integer> {
}
