package com.bustrack.strategy;

import com.bustrack.model.trip.Trip;

/**
 * Chiến lược ưu đãi sinh viên về quê (Giảm 10%)
 * (OOP: Strategy Pattern)
 */
public class StudentDiscountPricingStrategy implements IPricingStrategy {
    private static final double DISCOUNT_RATE = 0.90; // Giảm 10%

    @Override
    public double calculatePrice(Trip trip, int seatCount) {
        if (trip == null || seatCount <= 0) return 0;
        return Math.round(trip.getBasePrice() * seatCount * DISCOUNT_RATE);
    }

    @Override
    public String getStrategyName() {
        return "Ưu đãi sinh viên về quê (Giảm 10%)";
    }
}
