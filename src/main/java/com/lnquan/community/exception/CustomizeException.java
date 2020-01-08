package com.lnquan.community.exception;

import com.lnquan.community.utils.ExceptionStatus;

public class CustomizeException extends RuntimeException {
    private ExceptionStatus exceptionStatus;
    public CustomizeException(ExceptionStatus exceptionStatus){
        this .exceptionStatus = exceptionStatus;
    }

    public int getStatus() {
        return exceptionStatus.getStatus();
    }

    @Override
    public String getMessage() {
        return this.exceptionStatus.getMessage();
    }
}
