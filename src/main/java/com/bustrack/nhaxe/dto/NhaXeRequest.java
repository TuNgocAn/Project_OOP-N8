package vn.busapp.nhaxe.dto;

import jakarta.validation.constraints.NotBlank;

/** maUser chỉ dùng khi tạo (POST); PUT bỏ qua. */
public record NhaXeRequest(Long maUser, @NotBlank String tenNhaXe, String diaChi, String soDienThoai, String moTa) {}
