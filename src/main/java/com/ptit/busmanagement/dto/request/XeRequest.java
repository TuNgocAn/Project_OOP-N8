package com.ptit.busmanagement.dto.request;

import com.ptit.busmanagement.entity.TrangThaiXe;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record XeRequest(
        @NotBlank(message = "Biển số xe không được để trống") String bienSo,
        @NotBlank(message = "Loại xe không được để trống") String loaiXe,
        @NotNull(message = "Số chỗ không được để trống") @Min(value = 1, message = "Số chỗ tối thiểu là 1") Integer soCho,
        @NotNull(message = "Mã nhà xe không được để trống") Long maNhaXe,
        TrangThaiXe trangThai
) {}
