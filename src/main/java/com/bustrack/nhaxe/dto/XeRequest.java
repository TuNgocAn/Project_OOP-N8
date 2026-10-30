package vn.busapp.nhaxe.dto;

import jakarta.validation.constraints.*;
import vn.busapp.nhaxe.entity.XeTrangThai;

public record XeRequest(
        @NotBlank String bienSo,
        @NotBlank String loaiXe,
        @NotNull @Min(1) Integer soCho,
        @NotNull Long maNhaXe,
        XeTrangThai trangThai) {}
