package com.request;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
public class AdminChangePasswordRequest {
    private Integer userId;

    private String password;
}
