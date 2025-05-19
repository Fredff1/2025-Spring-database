package com.repairhub.management.auth.exception;

import org.springframework.http.HttpStatus;

import com.repairhub.management.common.enums.ErrorCode;
import com.repairhub.management.common.exception.BaseException;

public class InvalidPasswordException extends BaseException{
    public InvalidPasswordException(String message){
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PASSWORD);
    }
}
