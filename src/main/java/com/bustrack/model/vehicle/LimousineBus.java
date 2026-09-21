package com.bustrack.model.vehicle;

/**
 * Xe Limousine VIP hạng sang (OOP: Inheritance)
 */
public class LimousineBus extends Vehicle {
    private String vipAmenities; // Tiện ích VIP: Ghế massage, màn hình riêng...

    public LimousineBus() {
        super();
        setVehicleType(VehicleType.LIMOUSINE);
        this.vipAmenities = "Ghế massage đa điểm, màn hình giải trí riêng, đèn đọc sách, sạc không dây";
    }

    public LimousineBus(String id, String licensePlate, String operatorId, int totalSeats, String stationName, String vipAmenities) {
        super(id, licensePlate, operatorId, VehicleType.LIMOUSINE, totalSeats, stationName);
        this.vipAmenities = vipAmenities;
    }

    @Override
    public String getAmenityDescription() {
        return "Khoang thương gia VIP: " + vipAmenities;
    }

    @Override
    public double getPriceMultiplier() {
        return 1.5; // Phụ thu 50% cho xe limousine
    }

    public String getVipAmenities() {
        return vipAmenities;
    }

    public void setVipAmenities(String vipAmenities) {
        this.vipAmenities = vipAmenities;
    }
}
