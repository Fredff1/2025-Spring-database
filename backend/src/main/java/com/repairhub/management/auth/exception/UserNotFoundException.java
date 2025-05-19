package com.repairhub.management.auth.exception;

import org.springframework.http.HttpStatus;

import com.repairhub.management.common.enums.ErrorCode;
import com.repairhub.management.common.exception.BaseException;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND);
    }
}
