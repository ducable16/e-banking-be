package com.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_valid")
    private Boolean isValid = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

}
