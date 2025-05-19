package com.repairhub.management.common.exception;

import org.springframework.http.HttpStatus;

import com.repairhub.management.common.enums.ErrorCode;

public class BaseException extends RuntimeException{

    private HttpStatus status;

    private ErrorCode code;

    public BaseException(String message,HttpStatus status,ErrorCode code){
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus(){
        return status;
    }

    public ErrorCode getCode(){
        return code;
    }
}
