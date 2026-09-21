package com.bustrack.service;

import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface cho dịch vụ Quản lý chuyến xe (OOP: Abstraction)
 */
public interface ITripService {
    Trip createTrip(String operatorId, String operatorName, String vehicleId, String licensePlate,
                   com.bustrack.model.vehicle.VehicleType vehicleType, Driver driver,
                   String departureLocation, String destinationLocation, LocalDateTime departureTime,
                   double estimatedDurationHours, double basePrice, int totalSeats);

    boolean updateTrip(Trip trip);
    boolean deleteTrip(String tripId);
    Optional<Trip> getTripById(String tripId);
    List<Trip> getTripsByOperator(String operatorId);
    List<Trip> searchTrips(String departure, String destination, LocalDate date);
    boolean assignDriver(String tripId, Driver driver);
    boolean updateTripStatus(String tripId, TripStatus status);
    List<String> getPopularDestinations();
    List<String> getPopularDepartures();
}
