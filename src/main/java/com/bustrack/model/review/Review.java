package com.bustrack.model.review;

import java.time.LocalDateTime;

/**
 * Đánh giá của khách hàng về nhà xe và chuyến xe
 * Đáp ứng yêu cầu: "đánh giá (nếu database không quá to)"
 */
public class Review {
    private String id;
    private String operatorId;   // ID nhà xe được đánh giá
    private String tripId;       // Mã chuyến xe (nếu có)
    private String customerId;   // ID khách hàng
    private String customerName; // Tên khách hàng
    private int rating;          // Điểm đánh giá: 1 đến 5 sao
    private String comment;      // Lời nhận xét
    private LocalDateTime createdAt;

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(String id, String operatorId, String tripId, String customerId, 
                  String customerName, int rating, String comment) {
        this.id = id;
        this.operatorId = operatorId;
        this.tripId = tripId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.rating = Math.max(1, Math.min(5, rating)); // Đảm bảo rating từ 1-5
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = Math.max(1, Math.min(5, rating));
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
