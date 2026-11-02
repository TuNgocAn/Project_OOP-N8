package com.ptit.busmanagement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DanhGiaRequest(
        @NotNull(message = "Mã chuyến xe không được để trống") Long maChuyen,
        Long maKhachHang,
        @NotBlank(message = "Tên khách hàng không được để trống") String tenKhachHang,
        @NotNull(message = "Số sao không được để trống") @Min(1) @Max(5) Integer soSao,
        String noiDung
) {}
