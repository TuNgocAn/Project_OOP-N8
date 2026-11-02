package com.ptit.busmanagement.dto.request;

import com.ptit.busmanagement.entity.TrangThaiXe;
import jakarta.validation.constraints.NotNull;

public record XeTrangThaiRequest(
        @NotNull(message = "Trạng thái xe không được để trống") TrangThaiXe trangThai
) {}
