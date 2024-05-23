package com.harry.userservice.exception;

public class CommonException extends RuntimeException {

    private String message;
    private Object data;

    public CommonException() {
    }

    public CommonException(String msg, Object data) {
        super(msg);
        this.message = msg;
        this.data = data;
        System.out.println("Exception message ==> " + message);
    }

    public Object getData() {
        return data;
    }
}