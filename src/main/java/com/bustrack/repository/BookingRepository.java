package com.bustrack.repository;

import com.bustrack.model.booking.Booking;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository lưu trữ và truy vấn Đặt vé của khách hàng
 */
public class BookingRepository extends AbstractJsonRepository<Booking> {
    private static final String FILE_PATH = "data/bookings.json";

    public BookingRepository() {
        super(FILE_PATH, new TypeToken<List<Booking>>() {}.getType());
    }

    public BookingRepository(String customPath) {
        super(customPath, new TypeToken<List<Booking>>() {}.getType());
    }

    @Override
    protected String getId(Booking entity) {
        return entity.getId();
    }

    public List<Booking> findByCustomerId(String customerId) {
        if (customerId == null) return List.of();
        return items.stream()
                .filter(b -> customerId.equals(b.getCustomerId()))
                .collect(Collectors.toList());
    }

    public List<Booking> findByTripId(String tripId) {
        if (tripId == null) return List.of();
        return items.stream()
                .filter(b -> tripId.equals(b.getTripId()))
                .collect(Collectors.toList());
    }
}
