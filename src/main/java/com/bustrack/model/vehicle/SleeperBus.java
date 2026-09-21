package com.bustrack.model.vehicle;

/**
 * Xe khách giường nằm 2 tầng chất lượng cao (OOP: Inheritance)
 */
public class SleeperBus extends Vehicle {
    private int bunkLevels; // Số tầng giường (thường là 2 tầng)

    public SleeperBus() {
        super();
        setVehicleType(VehicleType.SLEEPER);
        this.bunkLevels = 2;
    }

    public SleeperBus(String id, String licensePlate, String operatorId, int totalSeats, String stationName, int bunkLevels) {
        super(id, licensePlate, operatorId, VehicleType.SLEEPER, totalSeats, stationName);
        this.bunkLevels = bunkLevels;
    }

    @Override
    public String getAmenityDescription() {
        return "Giường da cao cấp " + bunkLevels + " tầng, chăn gối sạch sẽ, cổng sạc USB, Wifi tốc độ cao";
    }

    @Override
    public double getPriceMultiplier() {
        return 1.25; // Phụ thu 25% cho xe giường nằm
    }

    public int getBunkLevels() {
        return bunkLevels;
    }

    public void setBunkLevels(int bunkLevels) {
        this.bunkLevels = bunkLevels;
    }
}
