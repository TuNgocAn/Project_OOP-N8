package com.bustrack.model.trip;

/**
 * Trạng thái của chuyến xe theo thời gian thực
 */
public enum TripStatus {
    SCHEDULED("Sắp khởi hành"),
    RUNNING("Đang chạy trên đường"),
    COMPLETED("Đã đến bến / Hoàn thành"),
    CANCELLED("Đã hủy chuyến");

    private final String displayName;

    TripStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
