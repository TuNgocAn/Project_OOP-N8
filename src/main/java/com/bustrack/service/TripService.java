package com.bustrack.service;

import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.bustrack.model.vehicle.VehicleStatus;
import com.bustrack.model.vehicle.VehicleType;
import com.bustrack.repository.TripRepository;
import com.bustrack.repository.VehicleRepository;
import com.bustrack.util.ValidationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Lớp cài đặt dịch vụ quản lý chuyến xe và tài xế
 */
public class TripService implements ITripService {
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;

    public TripService(TripRepository tripRepository, VehicleRepository vehicleRepository) {
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Trip createTrip(String operatorId, String operatorName, String vehicleId, String licensePlate,
                          VehicleType vehicleType, Driver driver, String departureLocation, 
                          String destinationLocation, LocalDateTime departureTime,
                          double estimatedDurationHours, double basePrice, int totalSeats) {
        if (!ValidationUtils.isNotEmpty(departureLocation) || !ValidationUtils.isNotEmpty(destinationLocation)) {
            throw new IllegalArgumentException("Điểm đi và điểm đến không được để trống!");
        }
        if (departureTime == null || departureTime.isBefore(LocalDateTime.now().minusHours(1))) {
            throw new IllegalArgumentException("Giờ xuất bến không hợp lệ!");
        }
        if (estimatedDurationHours <= 0) {
            throw new IllegalArgumentException("Thời gian di chuyển phải lớn hơn 0 giờ!");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Giá vé phải lớn hơn 0 VNĐ!");
        }
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("Số chỗ ngồi phải lớn hơn 0!");
        }

        String id = "TRIP_" + UUID.randomUUID().toString().substring(0, 8);
        Trip trip = new Trip(id, operatorId, operatorName, vehicleId, licensePlate, vehicleType, driver,
                departureLocation.trim(), destinationLocation.trim(), departureTime, 
                estimatedDurationHours, basePrice, totalSeats);

        tripRepository.save(trip);
        return trip;
    }

    @Override
    public boolean updateTrip(Trip trip) {
        if (trip == null) return false;
        return tripRepository.update(trip);
    }

    @Override
    public boolean deleteTrip(String tripId) {
        return tripRepository.deleteById(tripId);
    }

    @Override
    public Optional<Trip> getTripById(String tripId) {
        return tripRepository.findById(tripId);
    }

    @Override
    public List<Trip> getTripsByOperator(String operatorId) {
        return tripRepository.findByOperatorId(operatorId);
    }

    @Override
    public List<Trip> searchTrips(String departure, String destination, LocalDate date) {
        return tripRepository.searchTrips(departure, destination, date);
    }

    @Override
    public boolean assignDriver(String tripId, Driver driver) {
        Optional<Trip> opt = tripRepository.findById(tripId);
        if (opt.isPresent()) {
            Trip trip = opt.get();
            trip.setDriver(driver);
            return tripRepository.update(trip);
        }
        return false;
    }

    @Override
    public boolean updateTripStatus(String tripId, TripStatus status) {
        Optional<Trip> opt = tripRepository.findById(tripId);
        if (opt.isPresent()) {
            Trip trip = opt.get();
            trip.setStatus(status);

            // Đồng bộ trạng thái xe với trạng thái chuyến
            if (trip.getVehicleId() != null && vehicleRepository != null) {
                if (status == TripStatus.RUNNING) {
                    vehicleRepository.findById(trip.getVehicleId()).ifPresent(v -> {
                        v.setStatus(VehicleStatus.IN_TRANSIT);
                        vehicleRepository.update(v);
                    });
                } else if (status == TripStatus.COMPLETED) {
                    vehicleRepository.findById(trip.getVehicleId()).ifPresent(v -> {
                        v.setStatus(VehicleStatus.AT_STATION);
                        v.setStationName(trip.getDestinationLocation());
                        vehicleRepository.update(v);
                    });
                }
            }

            return tripRepository.update(trip);
        }
        return false;
    }

    @Override
    public List<String> getPopularDestinations() {
        return tripRepository.findAll().stream()
                .map(Trip::getDestinationLocation)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPopularDepartures() {
        return tripRepository.findAll().stream()
                .map(Trip::getDepartureLocation)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
