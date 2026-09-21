package com.bustrack.model.booking;

/**
 * Trạng thái của vé đã đặt
 */
public enum BookingStatus {
    CONFIRMED("Đã xác nhận"),
    CANCELLED("Đã hủy vé");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
