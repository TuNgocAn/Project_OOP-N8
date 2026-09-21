package com.bustrack;

import com.bustrack.model.vehicle.Vehicle;
import com.bustrack.model.vehicle.VehicleStatus;
import com.bustrack.model.vehicle.VehicleType;
import com.bustrack.repository.VehicleRepository;
import com.bustrack.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kiểm thử nghiệp vụ quản lý xe, xe đến, xe đi, số lượng xe còn ở bến
 */
public class VehicleServiceTest {
    private static final String TEST_VEHICLES = "data/test_vehicles_service.json";
    private VehicleRepository vehicleRepo;
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        cleanFiles();
        vehicleRepo = new VehicleRepository(TEST_VEHICLES);
        vehicleService = new VehicleService(vehicleRepo);
    }

    @AfterEach
    void tearDown() {
        cleanFiles();
    }

    private void cleanFiles() {
        new File(TEST_VEHICLES).delete();
    }

    @Test
    @DisplayName("Thêm xe và theo dõi số lượng xe ở bến / trên đường")
    void testVehicleTrackingAtStationAndInTransit() {
        String opId = "OP_TEST";

        // Thêm 3 xe ban đầu ở bến
        Vehicle v1 = vehicleService.addVehicle(opId, "29B-111.11", VehicleType.LIMOUSINE, 24, "Bến xe Giáp Bát");
        Vehicle v2 = vehicleService.addVehicle(opId, "29B-222.22", VehicleType.SLEEPER, 40, "Bến xe Giáp Bát");
        Vehicle v3 = vehicleService.addVehicle(opId, "29B-333.33", VehicleType.SEATER, 29, "Bến xe Giáp Bát");

        assertEquals(3, vehicleService.countVehiclesAtStation(opId));
        assertEquals(0, vehicleService.countVehiclesInTransit(opId));

        // 1 xe xuất bến chạy trên đường
        vehicleService.updateVehicleStatus(v1.getId(), VehicleStatus.IN_TRANSIT);
        assertEquals(2, vehicleService.countVehiclesAtStation(opId));
        assertEquals(1, vehicleService.countVehiclesInTransit(opId));

        // 1 xe vào bảo dưỡng
        vehicleService.updateVehicleStatus(v2.getId(), VehicleStatus.MAINTENANCE);
        assertEquals(1, vehicleService.countVehiclesAtStation(opId));
        assertEquals(1, vehicleService.countVehiclesInTransit(opId));
        assertEquals(1, vehicleService.countVehiclesInMaintenance(opId));

        // Xe hoàn thành chuyến, về đến bến
        vehicleService.updateVehicleStatus(v1.getId(), VehicleStatus.AT_STATION);
        assertEquals(2, vehicleService.countVehiclesAtStation(opId));
        assertEquals(0, vehicleService.countVehiclesInTransit(opId));
    }
}
