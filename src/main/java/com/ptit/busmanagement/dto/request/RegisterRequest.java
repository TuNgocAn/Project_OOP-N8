package com.ptit.busmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username không được để trống")
        @Size(min = 3, max = 50, message = "Username từ 3 đến 50 ký tự")
        String username,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
        String password,

        String email,
        String soDienThoai,
        String vaiTro, // ADMIN, NHA_XE, KHACH_HANG
        String hoTen,
        String tenNhaXe,
        String diaChi
) {}
