package com.ptit.busmanagement.entity;

import com.ptit.busmanagement.exception.ValidationException;

/**
 * Trạng thái xe bến bãi theo yêu cầu nghiệp vụ:
 * - CON_O_BEN: Xe đang đỗ tại bãi/bến
 * - XE_DEN: Xe vừa cập bến/đến bến
 * - XE_DI: Xe vừa xuất bến/rời bến
 * - BAO_TRI: Xe đang kiểm tra, bảo trì kĩ thuật
 */
public enum TrangThaiXe {
    CON_O_BEN("Còn ở bến"),
    XE_DEN("Xe đến"),
    XE_DI("Xe đi"),
    BAO_TRI("Bảo dưỡng");

    private final String nhan;

    TrangThaiXe(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }

    public static TrangThaiXe parse(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            throw new ValidationException("Trạng thái xe không hợp lệ: " + s + " (CON_O_BEN, XE_DEN, XE_DI, BAO_TRI)");
        }
    }
}
