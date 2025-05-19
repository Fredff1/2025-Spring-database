package com.repairhub.management.auth.exception;

import org.springframework.http.HttpStatus;

import com.repairhub.management.common.enums.ErrorCode;
import com.repairhub.management.common.exception.BaseException;

public class DuplicatedUserException extends BaseException{
    public DuplicatedUserException(String message){
        super(message, HttpStatus.CONFLICT, ErrorCode.DUPLICATED_USER);
    }
}
