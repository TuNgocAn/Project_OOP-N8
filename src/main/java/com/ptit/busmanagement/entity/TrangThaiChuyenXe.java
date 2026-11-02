package com.ptit.busmanagement.entity;

public enum TrangThaiChuyenXe {
    SAP_KHOI_HANH("Sắp khởi hành"),
    DANG_CHAY("Đang chạy"),
    HOAN_THANH("Hoàn thành"),
    DA_HUY("Đã hủy");

    private final String nhan;

    TrangThaiChuyenXe(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
