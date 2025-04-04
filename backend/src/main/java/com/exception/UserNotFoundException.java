package com.exception;


import com.enums.ErrorCode;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(Integer userId) {
        super(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId);
    }
}
