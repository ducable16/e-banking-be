package com.exception;

import com.enums.ErrorCode;

public class WrongOtpException extends BaseException {

    public WrongOtpException() {
        super(ErrorCode.INVALID_OTP, "OTP is incorrect or expired");
    }
}
