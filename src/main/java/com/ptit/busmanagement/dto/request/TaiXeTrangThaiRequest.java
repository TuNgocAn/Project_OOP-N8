package com.ptit.busmanagement.dto.request;

import com.ptit.busmanagement.entity.TrangThaiTaiXe;
import jakarta.validation.constraints.NotNull;

public record TaiXeTrangThaiRequest(
        @NotNull(message = "Trạng thái tài xế không được để trống") TrangThaiTaiXe trangThai
) {}
