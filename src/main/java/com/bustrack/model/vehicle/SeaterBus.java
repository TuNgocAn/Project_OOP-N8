package com.bustrack.model.vehicle;

/**
 * Xe khách ghế ngồi (OOP: Inheritance)
 */
public class SeaterBus extends Vehicle {

    public SeaterBus() {
        super();
        setVehicleType(VehicleType.SEATER);
    }

    public SeaterBus(String id, String licensePlate, String operatorId, int totalSeats, String stationName) {
        super(id, licensePlate, operatorId, VehicleType.SEATER, totalSeats, stationName);
    }

    @Override
    public String getAmenityDescription() {
        return "Ghế đệm êm ái, điều hòa mát lạnh, nước suối miễn phí";
    }

    @Override
    public double getPriceMultiplier() {
        return 1.0; // Giá vé tiêu chuẩn
    }
}
