package com.bustrack.strategy;

import com.bustrack.model.trip.Trip;

/**
 * Interface cho chiến lược tính giá vé (OOP: Strategy Pattern, Polymorphism)
 */
public interface IPricingStrategy {
    /**
     * Tính tổng giá vé dựa trên thông tin chuyến xe và số lượng ghế đặt
     */
    double calculatePrice(Trip trip, int seatCount);

    /**
     * Tên mô tả chiến lược giá
     */
    String getStrategyName();
}
