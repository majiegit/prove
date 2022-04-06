package com.csjs.coreapp.commen.exception;

/**
 * 自定义异常类
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}
