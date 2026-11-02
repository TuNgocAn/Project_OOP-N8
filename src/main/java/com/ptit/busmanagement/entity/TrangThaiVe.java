package com.ptit.busmanagement.entity;

public enum TrangThaiVe {
    DA_DAT("Đã đặt"),
    DA_HUY("Đã hủy");

    private final String nhan;

    TrangThaiVe(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }
}
