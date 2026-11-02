package com.ptit.busmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TuyenXeRequest(
        @NotBlank(message = "Tên tuyến xe không được để trống") String tenTuyen,
        @NotBlank(message = "Điểm đi không được để trống") String diemDi,
        @NotBlank(message = "Điểm đến không được để trống") String diemDen,
        Integer thoiGianDuKienPhut,
        Double khoangCachKm
) {}
