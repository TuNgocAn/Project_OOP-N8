package com.javaweb.model;

/** Thông tin ngắn về người đang đăng nhập (không bao giờ chứa mật khẩu). */
public class UserInfoResponse {

    private Integer maUser;
    private String username;
    private String email;
    private String hoTen;

    public UserInfoResponse(Integer maUser, String username, String email, String hoTen) {
        this.maUser = maUser;
        this.username = username;
        this.email = email;
        this.hoTen = hoTen;
    }

    public Integer getMaUser() {
        return maUser;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHoTen() {
        return hoTen;
    }
}
