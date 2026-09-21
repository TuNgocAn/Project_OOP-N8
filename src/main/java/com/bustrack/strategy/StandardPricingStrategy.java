package com.bustrack.strategy;

import com.bustrack.model.trip.Trip;

/**
 * Chiến lược giá vé tiêu chuẩn ngày thường (OOP: Polymorphism)
 */
public class StandardPricingStrategy implements IPricingStrategy {

    @Override
    public double calculatePrice(Trip trip, int seatCount) {
        if (trip == null || seatCount <= 0) return 0;
        return trip.getBasePrice() * seatCount;
    }

    @Override
    public String getStrategyName() {
        return "Giá ngày thường tiêu chuẩn";
    }
}
