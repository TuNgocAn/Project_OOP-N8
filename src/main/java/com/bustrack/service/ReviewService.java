package com.bustrack.service;

import com.bustrack.model.review.Review;
import com.bustrack.repository.ReviewRepository;

import java.util.List;
import java.util.UUID;

/**
 * Lớp cài đặt dịch vụ Đánh giá nhận xét
 */
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review addReview(String operatorId, String tripId, String customerId, 
                            String customerName, int rating, String comment) {
        String id = "REV_" + UUID.randomUUID().toString().substring(0, 8);
        Review review = new Review(id, operatorId, tripId, customerId, customerName, rating, comment);
        reviewRepository.save(review);
        return review;
    }

    @Override
    public List<Review> getReviewsByOperator(String operatorId) {
        return reviewRepository.findByOperatorId(operatorId);
    }

    @Override
    public double getAverageRating(String operatorId) {
        return reviewRepository.getAverageRatingByOperator(operatorId);
    }
}
