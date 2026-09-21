package com.bustrack.repository;

import com.bustrack.model.vehicle.Vehicle;
import com.bustrack.model.vehicle.VehicleStatus;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository lưu trữ và truy vấn Phương tiện xe khách
 * Hỗ trợ theo dõi xe đến, xe đi, số lượng xe còn ở bến
 */
public class VehicleRepository extends AbstractJsonRepository<Vehicle> {
    private static final String FILE_PATH = "data/vehicles.json";

    public VehicleRepository() {
        super(FILE_PATH, new TypeToken<List<Vehicle>>() {}.getType());
    }

    public VehicleRepository(String customPath) {
        super(customPath, new TypeToken<List<Vehicle>>() {}.getType());
    }

    @Override
    protected String getId(Vehicle entity) {
        return entity.getId();
    }

    public List<Vehicle> findByOperatorId(String operatorId) {
        if (operatorId == null) return List.of();
        return items.stream()
                .filter(v -> operatorId.equals(v.getOperatorId()))
                .collect(Collectors.toList());
    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        if (licensePlate == null) return Optional.empty();
        return items.stream()
                .filter(v -> licensePlate.equalsIgnoreCase(v.getLicensePlate()))
                .findFirst();
    }

    public long countByOperatorAndStatus(String operatorId, VehicleStatus status) {
        if (operatorId == null || status == null) return 0;
        return items.stream()
                .filter(v -> operatorId.equals(v.getOperatorId()) && v.getStatus() == status)
                .count();
    }
}
