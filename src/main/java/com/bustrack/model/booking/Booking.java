package com.bustrack.model.booking;

import java.time.LocalDateTime;

/**
 * Lớp đại diện cho việc đặt chỗ / vé xe của khách hàng (OOP: Encapsulation)
 */
public class Booking {
    private String id;
    private String tripId;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private int seatCount;
    private double totalPrice;
    private LocalDateTime bookingTime;
    private BookingStatus status;
    private String pickupPoint; // Điểm đón khách cụ thể

    public Booking() {
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    public Booking(String id, String tripId, String customerId, String customerName, 
                   String customerPhone, int seatCount, double totalPrice, String pickupPoint) {
        this.id = id;
        this.tripId = tripId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.seatCount = seatCount;
        this.totalPrice = totalPrice;
        this.pickupPoint = pickupPoint;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }
}
