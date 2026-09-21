package com.bustrack.service;

import com.bustrack.model.booking.Booking;
import com.bustrack.strategy.IPricingStrategy;

import java.util.List;
import java.util.Optional;

/**
 * Interface cho dịch vụ Đặt vé xe (OOP: Abstraction)
 */
public interface IBookingService {
    Booking bookTicket(String tripId, String customerId, String customerName, 
                       String customerPhone, int seatCount, String pickupPoint, 
                       IPricingStrategy pricingStrategy);

    boolean cancelBooking(String bookingId);
    boolean deleteBooking(String bookingId);
    int deleteCancelledBookingsByCustomer(String customerId);
    Optional<Booking> getBookingById(String bookingId);
    List<Booking> getBookingsByCustomer(String customerId);
    List<Booking> getBookingsByTrip(String tripId);
}
