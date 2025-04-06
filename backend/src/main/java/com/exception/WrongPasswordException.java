package com.exception;

import com.enums.ErrorCode;

public class WrongPasswordException extends BaseException {
    public WrongPasswordException() {
        super(ErrorCode.WRONG_PASSWORD, "Wrong password");
    }

}
