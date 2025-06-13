package com.repairhub.management.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.repairhub.management.common.dto.CommonErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    
    
    //@ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorResponse> handleAllException(Exception e){
        CommonErrorResponse resp = new CommonErrorResponse(500, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
