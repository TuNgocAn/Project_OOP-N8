package com.ptit.busmanagement.entity;

import com.ptit.busmanagement.exception.ValidationException;

public enum TrangThaiTaiXe {
    RANH("Rảnh"),
    DANG_LAI("Đang lái"),
    NGHI("Nghỉ");

    private final String nhan;

    TrangThaiTaiXe(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }

    public static TrangThaiTaiXe parse(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            throw new ValidationException("Trạng thái tài xế không hợp lệ: " + s + " (RANH, DANG_LAI, NGHI)");
        }
    }
}
