package com.ptit.busmanagement.dto.request;

import com.ptit.busmanagement.entity.TrangThaiTaiXe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaiXeRequest(
        @NotBlank(message = "Họ tên tài xế không được để trống") String hoTen,
        String soDienThoai,
        @NotBlank(message = "Số GPLX không được để trống") String soGPLX,
        String loaiBang,
        @NotNull(message = "Mã nhà xe không được để trống") Long maNhaXe,
        TrangThaiTaiXe trangThai
) {}
