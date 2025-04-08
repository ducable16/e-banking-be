package com.exception;


import com.enums.ErrorCode;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND, "Entity not found");
    }
}
