package com.bustrack.model.vehicle;

/**
 * Trạng thái phương tiện tại bến và trên đường
 */
public enum VehicleStatus {
    AT_STATION("Đang ở bến"),
    IN_TRANSIT("Đang di chuyển"),
    MAINTENANCE("Đang bảo dưỡng");

    private final String displayName;

    VehicleStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
