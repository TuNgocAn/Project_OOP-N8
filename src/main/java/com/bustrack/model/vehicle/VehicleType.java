package com.bustrack.model.vehicle;

/**
 * Loại xe phục vụ chuyến
 */
public enum VehicleType {
    SEATER("Xe ghế ngồi"),
    SLEEPER("Xe giường nằm"),
    LIMOUSINE("Xe Limousine VIP");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
