package com.bustrack.service;

import com.bustrack.model.review.Review;

import java.util.List;

/**
 * Interface cho dịch vụ Đánh giá nhận xét (OOP: Abstraction)
 */
public interface IReviewService {
    Review addReview(String operatorId, String tripId, String customerId, 
                     String customerName, int rating, String comment);
    List<Review> getReviewsByOperator(String operatorId);
    double getAverageRating(String operatorId);
}
