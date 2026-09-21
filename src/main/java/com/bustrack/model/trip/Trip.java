package com.bustrack.model.trip;

import com.bustrack.model.vehicle.VehicleType;
import java.time.LocalDateTime;

/**
 * Lớp đại diện cho Chuyến xe khách về quê
 * Đáp ứng yêu cầu: chặng đi, giờ xe chạy, thời gian di chuyển, giá vé, tài xế
 */
public class Trip {
    private String id;
    private String operatorId;          // ID nhà xe
    private String operatorName;        // Tên nhà xe
    private String vehicleId;           // ID xe
    private String licensePlate;        // Biển số xe
    private VehicleType vehicleType;    // Loại xe
    private Driver driver;              // Tài xế phụ trách chuyến xe
    private String departureLocation;   // Nơi xuất phát (ví dụ: Bến xe Giáp Bát - Hà Nội)
    private String destinationLocation; // Nơi đến (ví dụ: Bến xe Nam Định)
    private LocalDateTime departureTime;// Giờ xuất bến
    private double estimatedDurationHours; // Thời gian di chuyển (giờ), ví dụ: 2.0 giờ
    private double basePrice;           // Giá vé cơ bản (VNĐ)
    private int totalSeats;             // Tổng số vé/chỗ
    private int availableSeats;         // Số vé còn trống
    private TripStatus status;          // Trạng thái chuyến

    public Trip() {
        this.status = TripStatus.SCHEDULED;
    }

    public Trip(String id, String operatorId, String operatorName, String vehicleId, String licensePlate,
                VehicleType vehicleType, Driver driver, String departureLocation, String destinationLocation,
                LocalDateTime departureTime, double estimatedDurationHours, double basePrice, int totalSeats) {
        this.id = id;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.driver = driver;
        this.departureLocation = departureLocation;
        this.destinationLocation = destinationLocation;
        this.departureTime = departureTime;
        this.estimatedDurationHours = estimatedDurationHours;
        this.basePrice = basePrice;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.status = TripStatus.SCHEDULED;
    }

    /**
     * Tính toán giờ xe đến nơi dự kiến dựa trên giờ xuất bến và thời gian di chuyển
     */
    public LocalDateTime getEstimatedArrivalTime() {
        if (departureTime == null) return null;
        long minutes = Math.round(estimatedDurationHours * 60);
        return departureTime.plusMinutes(minutes);
    }

    /**
     * Đặt chỗ và giảm số ghế trống khả dụng
     */
    public boolean bookSeats(int count) {
        if (count > 0 && availableSeats >= count) {
            availableSeats -= count;
            return true;
        }
        return false;
    }

    /**
     * Hủy vé và hoàn lại số ghế trống
     */
    public void releaseSeats(int count) {
        availableSeats = Math.min(totalSeats, availableSeats + count);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public double getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(double estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }
}
