package com.bustrack;

import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.vehicle.VehicleType;
import com.bustrack.strategy.HolidayPricingStrategy;
import com.bustrack.strategy.IPricingStrategy;
import com.bustrack.strategy.StandardPricingStrategy;
import com.bustrack.strategy.StudentDiscountPricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Kiểm thử tính đa hình và chiến lược giá (Strategy Pattern Test)
 */
public class PricingStrategyTest {
    private Trip sampleTrip;

    @BeforeEach
    void setUp() {
        Driver driver = new Driver("DRV_01", "Nguyễn Văn Hùng", "0912345678", "GPLX-E-12345", 10);
        sampleTrip = new Trip("T01", "OP01", "Hoàng Long", "V01", "29B-123.45",
                VehicleType.LIMOUSINE, driver, "Hà Nội", "Nam Định",
                LocalDateTime.now().plusDays(1), 2.0, 100000.0, 24);
    }

    @Test
    @DisplayName("Kiểm tra chiến lược giá tiêu chuẩn")
    void testStandardPricing() {
        IPricingStrategy strategy = new StandardPricingStrategy();
        double priceFor2Seats = strategy.calculatePrice(sampleTrip, 2);
        assertEquals(200000.0, priceFor2Seats, 0.001);
    }

    @Test
    @DisplayName("Kiểm tra ưu đãi sinh viên giảm 10%")
    void testStudentDiscountPricing() {
        IPricingStrategy strategy = new StudentDiscountPricingStrategy();
        double priceFor2Seats = strategy.calculatePrice(sampleTrip, 2);
        // 200,000 * 0.9 = 180,000
        assertEquals(180000.0, priceFor2Seats, 0.001);
    }

    @Test
    @DisplayName("Kiểm tra giá vé dịp lễ Tết phụ thu 20%")
    void testHolidayPricing() {
        IPricingStrategy strategy = new HolidayPricingStrategy();
        double priceFor2Seats = strategy.calculatePrice(sampleTrip, 2);
        // 200,000 * 1.2 = 240,000
        assertEquals(240000.0, priceFor2Seats, 0.001);
    }
}
