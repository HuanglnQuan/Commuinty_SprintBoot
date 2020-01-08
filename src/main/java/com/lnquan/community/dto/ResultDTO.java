package com.lnquan.community.dto;

import com.lnquan.community.exception.CustomizeException;
import com.lnquan.community.utils.ExceptionStatus;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private String message;
    private int status;
    private T data;

    public ResultDTO errorOf(ExceptionStatus exception){
        message = exception.getMessage();
        status = exception.getStatus();
        return this;
    }

    public ResultDTO success(){
        message = ExceptionStatus.SUCCESS.getMessage();
        status = ExceptionStatus.SUCCESS.getStatus();
        return this;
    }

    public ResultDTO success(T data){
        this.data = data;
        message = ExceptionStatus.SUCCESS.getMessage();
        status = ExceptionStatus.SUCCESS.getStatus();
        return this;
    }

    public ResultDTO errorOf(CustomizeException e) {
        message = e.getMessage();
        status = e.getStatus();
        return this;
    }
}
