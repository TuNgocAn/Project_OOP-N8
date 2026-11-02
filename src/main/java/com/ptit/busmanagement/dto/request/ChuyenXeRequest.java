package com.ptit.busmanagement.dto.request;

import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ChuyenXeRequest(
        @NotNull(message = "Mã nhà xe không được để trống") Long maNhaXe,
        @NotNull(message = "Mã xe không được để trống") Long maXe,
        @NotNull(message = "Mã tài xế không được để trống") Long maTaiXe,
        @NotNull(message = "Mã tuyến xe không được để trống") Long maTuyen,
        @NotNull(message = "Thời gian khởi hành không được để trống") LocalDateTime khoiHanh,
        LocalDateTime duKienDen,
        @NotNull(message = "Giá vé không được để trống") Long giaVe,
        TrangThaiChuyenXe trangThai
) {}
