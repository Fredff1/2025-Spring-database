package com.repairhub.management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static<T> CommonResponse<T> toResponse(Integer code,String message,T data){
        CommonResponse<T> commonResponse = new CommonResponse<>();
        commonResponse.setCode(code);
        commonResponse.setData(data);
        commonResponse.setMessage(message);
        return commonResponse;
    }
}
