package vn.busapp.nhaxe.dto;

import jakarta.validation.constraints.*;
import vn.busapp.nhaxe.entity.TaiXeTrangThai;

public record TaiXeRequest(
        @NotBlank String hoTen,
        String soDienThoai,
        @NotBlank String soGPLX,
        String loaiBang,
        @NotNull Long maNhaXe,
        TaiXeTrangThai trangThai) {}
