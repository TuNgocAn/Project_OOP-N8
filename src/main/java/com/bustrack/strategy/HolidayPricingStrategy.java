package com.bustrack.strategy;

import com.bustrack.model.trip.Trip;

/**
 * Chiến lược giá vé dịp lễ, Tết cao điểm về quê (phụ thu 20%)
 * (OOP: Strategy Pattern)
 */
public class HolidayPricingStrategy implements IPricingStrategy {
    private static final double SURCHARGE_RATE = 1.20; // Phụ thu 20%

    @Override
    public double calculatePrice(Trip trip, int seatCount) {
        if (trip == null || seatCount <= 0) return 0;
        return Math.round(trip.getBasePrice() * seatCount * SURCHARGE_RATE);
    }

    @Override
    public String getStrategyName() {
        return "Giá vé cao điểm Lễ / Tết (Phụ thu 20%)";
    }
}
