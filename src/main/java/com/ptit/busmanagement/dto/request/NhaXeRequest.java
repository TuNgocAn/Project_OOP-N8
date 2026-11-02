package com.ptit.busmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;

/** maUser chỉ dùng khi tạo (POST); PUT bỏ qua. */
public record NhaXeRequest(
        Long maUser,
        @NotBlank(message = "Tên nhà xe không được để trống") String tenNhaXe,
        String diaChi,
        String soDienThoai,
        String moTa,
        String trangThai
) {}
