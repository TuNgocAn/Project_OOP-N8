package com.javaweb.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

/** Lỗi nghiệp vụ: mang theo mã HTTP, thông báo và (tuỳ chọn) lỗi theo từng trường. */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final Map<String, String> errors;

    public ApiException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public ApiException(HttpStatus status, String message, Map<String, String> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
