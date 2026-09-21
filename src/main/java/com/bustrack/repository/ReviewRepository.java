package com.bustrack.repository;

import com.bustrack.model.review.Review;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository lưu trữ và truy vấn Đánh giá của khách hàng
 * Đáp ứng yêu cầu: "đánh giá (nếu database không quá to)"
 */
public class ReviewRepository extends AbstractJsonRepository<Review> {
    private static final String FILE_PATH = "data/reviews.json";

    public ReviewRepository() {
        super(FILE_PATH, new TypeToken<List<Review>>() {}.getType());
    }

    public ReviewRepository(String customPath) {
        super(customPath, new TypeToken<List<Review>>() {}.getType());
    }

    @Override
    protected String getId(Review entity) {
        return entity.getId();
    }

    public List<Review> findByOperatorId(String operatorId) {
        if (operatorId == null) return List.of();
        return items.stream()
                .filter(r -> operatorId.equals(r.getOperatorId()))
                .collect(Collectors.toList());
    }

    public List<Review> findByTripId(String tripId) {
        if (tripId == null) return List.of();
        return items.stream()
                .filter(r -> tripId.equals(r.getTripId()))
                .collect(Collectors.toList());
    }

    public double getAverageRatingByOperator(String operatorId) {
        List<Review> list = findByOperatorId(operatorId);
        if (list.isEmpty()) return 5.0; // Mặc định 5 sao nếu chưa có đánh giá
        return list.stream().mapToInt(Review::getRating).average().orElse(5.0);
    }
}
