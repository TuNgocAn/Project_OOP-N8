package com.bustrack.repository;

import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository lưu trữ và truy vấn Chuyến xe
 */
public class TripRepository extends AbstractJsonRepository<Trip> {
    private static final String FILE_PATH = "data/trips.json";

    public TripRepository() {
        super(FILE_PATH, new TypeToken<List<Trip>>() {}.getType());
    }

    public TripRepository(String customPath) {
        super(customPath, new TypeToken<List<Trip>>() {}.getType());
    }

    @Override
    protected String getId(Trip entity) {
        return entity.getId();
    }

    public List<Trip> findByOperatorId(String operatorId) {
        if (operatorId == null) return List.of();
        return items.stream()
                .filter(t -> operatorId.equals(t.getOperatorId()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm kiếm chuyến xe theo chặng đường mong muốn và thời gian đi
     * (Khách hàng: về quê có chuyến xe nào)
     */
    public List<Trip> searchTrips(String departureKeyword, String destinationKeyword, LocalDate departureDate) {
        return items.stream()
                .filter(t -> t.getStatus() != TripStatus.CANCELLED)
                .filter(t -> {
                    if (departureKeyword == null || departureKeyword.isBlank()) return true;
                    return t.getDepartureLocation().toLowerCase().contains(departureKeyword.toLowerCase().trim());
                })
                .filter(t -> {
                    if (destinationKeyword == null || destinationKeyword.isBlank()) return true;
                    return t.getDestinationLocation().toLowerCase().contains(destinationKeyword.toLowerCase().trim());
                })
                .filter(t -> {
                    if (departureDate == null) return true;
                    return t.getDepartureTime() != null && t.getDepartureTime().toLocalDate().isEqual(departureDate);
                })
                .collect(Collectors.toList());
    }

    public List<Trip> findByStatus(TripStatus status) {
        if (status == null) return List.of();
        return items.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }
}
