package com.ptit.busmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatVeRequest(
        @NotNull(message = "Mã chuyến xe không được để trống") Long maChuyen,
        Long maKhachHang,
        @NotBlank(message = "Tên khách hàng không được để trống") String tenKhachHang,
        @NotBlank(message = "Số điện thoại không được để trống") String soDienThoai,
        String email,
        @NotBlank(message = "Số ghế không được để trống") String soGhe
) {}
