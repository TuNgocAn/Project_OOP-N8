package com.bustrack.service;

import com.bustrack.model.vehicle.Vehicle;
import com.bustrack.model.vehicle.VehicleStatus;
import com.bustrack.model.vehicle.VehicleType;

import java.util.List;
import java.util.Optional;

/**
 * Interface cho dịch vụ Quản lý phương tiện (OOP: Abstraction)
 * Phục vụ nghiệp vụ: quản lý xe đến, xe đi, số lượng xe còn ở bến
 */
public interface IVehicleService {
    Vehicle addVehicle(String operatorId, String licensePlate, VehicleType type, int totalSeats, String stationName);
    boolean updateVehicle(Vehicle vehicle);
    boolean deleteVehicle(String vehicleId);
    Optional<Vehicle> getVehicleById(String vehicleId);
    List<Vehicle> getVehiclesByOperator(String operatorId);
    boolean updateVehicleStatus(String vehicleId, VehicleStatus newStatus);
    long countVehiclesAtStation(String operatorId);
    long countVehiclesInTransit(String operatorId);
    long countVehiclesInMaintenance(String operatorId);
}
