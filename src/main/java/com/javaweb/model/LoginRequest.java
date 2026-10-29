package com.javaweb.model;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    // Có thể là tên đăng nhập hoặc email
    @NotBlank(message = "Hãy nhập tên đăng nhập hoặc email.")
    private String username;

    @NotBlank(message = "Hãy nhập mật khẩu.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
