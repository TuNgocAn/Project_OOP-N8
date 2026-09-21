package com.bustrack.model.vehicle;

import java.time.LocalDateTime;

/**
 * Lớp cơ sở trừu tượng đại diện cho xe khách (OOP: Abstraction, Inheritance)
 */
public abstract class Vehicle {
    private String id;
    private String licensePlate;   // Biển số xe (ví dụ: 29B-123.45)
    private String operatorId;     // ID nhà xe sở hữu
    private VehicleType vehicleType;
    private int totalSeats;        // Tổng số chỗ ngồi / giường
    private VehicleStatus status;  // Trạng thái: Ở bến, Đang di chuyển, Bảo dưỡng
    private String stationName;    // Bến xe hiện tại đỗ (ví dụ: Bến xe Giáp Bát, Bến xe Nước Ngầm...)
    private LocalDateTime lastUpdated;

    public Vehicle() {
        this.status = VehicleStatus.AT_STATION;
        this.lastUpdated = LocalDateTime.now();
    }

    public Vehicle(String id, String licensePlate, String operatorId, VehicleType vehicleType, int totalSeats, String stationName) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.operatorId = operatorId;
        this.vehicleType = vehicleType;
        this.totalSeats = totalSeats;
        this.status = VehicleStatus.AT_STATION;
        this.stationName = stationName;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Phương thức trừu tượng trả về thông tin tiện ích theo từng loại xe (OOP: Polymorphism)
     */
    public abstract String getAmenityDescription();

    /**
     * Hệ số phụ thu giá vé tương ứng với chất lượng xe (OOP: Polymorphism)
     */
    public abstract double getPriceMultiplier();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return licensePlate + " (" + vehicleType.getDisplayName() + " - " + totalSeats + " chỗ - " + status.getDisplayName() + ")";
    }
}
