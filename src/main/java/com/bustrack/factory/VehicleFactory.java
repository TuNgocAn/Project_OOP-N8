package com.bustrack.factory;

import com.bustrack.model.vehicle.*;

/**
 * Factory khởi tạo các đối tượng phương tiện linh hoạt (OOP: Factory Method Pattern)
 */
public class VehicleFactory {

    public static Vehicle createVehicle(VehicleType type, String id, String licensePlate, 
                                        String operatorId, int totalSeats, String stationName) {
        if (type == null) {
            throw new IllegalArgumentException("Loại phương tiện không được để trống!");
        }

        switch (type) {
            case SEATER:
                return new SeaterBus(id, licensePlate, operatorId, totalSeats, stationName);
            case SLEEPER:
                return new SleeperBus(id, licensePlate, operatorId, totalSeats, stationName, 2);
            case LIMOUSINE:
                return new LimousineBus(id, licensePlate, operatorId, totalSeats, stationName, 
                        "Ghế bọc da massage, TV giải trí riêng, cổng sạc Type-C, tai nghe cao cấp");
            default:
                throw new IllegalArgumentException("Loại phương tiện không hợp lệ: " + type);
        }
    }
}
