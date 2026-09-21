package com.bustrack.service;

import com.bustrack.factory.VehicleFactory;
import com.bustrack.model.vehicle.Vehicle;
import com.bustrack.model.vehicle.VehicleStatus;
import com.bustrack.model.vehicle.VehicleType;
import com.bustrack.repository.VehicleRepository;
import com.bustrack.util.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lớp cài đặt dịch vụ quản lý xe của nhà xe
 */
public class VehicleService implements IVehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle addVehicle(String operatorId, String licensePlate, VehicleType type, int totalSeats, String stationName) {
        if (!ValidationUtils.isNotEmpty(operatorId)) {
            throw new IllegalArgumentException("Nhà xe không hợp lệ!");
        }
        if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Biển số xe không hợp lệ!");
        }
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("Số ghế phải lớn hơn 0!");
        }

        if (vehicleRepository.findByLicensePlate(licensePlate).isPresent()) {
            throw new IllegalArgumentException("Biển số xe " + licensePlate + " đã tồn tại trong hệ thống!");
        }

        String id = "VEH_" + UUID.randomUUID().toString().substring(0, 8);
        Vehicle vehicle = VehicleFactory.createVehicle(type, id, licensePlate.trim().toUpperCase(), 
                operatorId, totalSeats, stationName != null ? stationName.trim() : "Bến xe trung tâm");

        vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        return vehicleRepository.update(vehicle);
    }

    @Override
    public boolean deleteVehicle(String vehicleId) {
        return vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public Optional<Vehicle> getVehicleById(String vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    @Override
    public List<Vehicle> getVehiclesByOperator(String operatorId) {
        return vehicleRepository.findByOperatorId(operatorId);
    }

    @Override
    public boolean updateVehicleStatus(String vehicleId, VehicleStatus newStatus) {
        Optional<Vehicle> opt = vehicleRepository.findById(vehicleId);
        if (opt.isPresent()) {
            Vehicle vehicle = opt.get();
            vehicle.setStatus(newStatus);
            return vehicleRepository.update(vehicle);
        }
        return false;
    }

    @Override
    public long countVehiclesAtStation(String operatorId) {
        return vehicleRepository.countByOperatorAndStatus(operatorId, VehicleStatus.AT_STATION);
    }

    @Override
    public long countVehiclesInTransit(String operatorId) {
        return vehicleRepository.countByOperatorAndStatus(operatorId, VehicleStatus.IN_TRANSIT);
    }

    @Override
    public long countVehiclesInMaintenance(String operatorId) {
        return vehicleRepository.countByOperatorAndStatus(operatorId, VehicleStatus.MAINTENANCE);
    }
}
