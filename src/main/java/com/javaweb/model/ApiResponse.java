package com.javaweb.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Khuôn trả về chung của mọi API: { success, message, data, errors }. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Map<String, String> errors;

    private ApiResponse(boolean success, String message, T data, Map<String, String> errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<T>(true, message, data, null);
    }

    public static <T> ApiResponse<T> fail(String message, Map<String, String> errors) {
        return new ApiResponse<T>(false, message, null, errors);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
