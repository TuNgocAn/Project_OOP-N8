package com.bustrack.util;

import com.bustrack.factory.UserFactory;
import com.bustrack.factory.VehicleFactory;
import com.bustrack.model.booking.Booking;
import com.bustrack.model.booking.BookingStatus;
import com.bustrack.model.review.Review;
import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.bustrack.model.user.BusOperator;
import com.bustrack.model.user.Customer;
import com.bustrack.model.vehicle.Vehicle;
import com.bustrack.model.vehicle.VehicleStatus;
import com.bustrack.model.vehicle.VehicleType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Tự động tạo dữ liệu mẫu phong phú khi lần đầu chạy ứng dụng (Mock Data)
 */
public class MockDataInitializer {

    public static void initializeIfEmpty(AppContext ctx) {
        if (!ctx.getUserRepository().findAll().isEmpty()) {
            return; // Đã có dữ liệu, không cần tạo lại
        }

        System.out.println("Dang khoi tao du lieu mau cho he thong BusTrack...");

        // 1. Tạo tài khoản Nhà xe mẫu
        BusOperator opHoangLong = UserFactory.createOperator(
                "OP_HOANGLONG", "hoanglong", "123456", "Đại diện Hoàng Long", "0988111222",
                "Nhà xe Hoàng Long", "0988111222", "Bến xe Giáp Bát, Giải Phóng, Hà Nội"
        );
        ctx.getUserRepository().save(opHoangLong);

        BusOperator opPhuongTrang = UserFactory.createOperator(
                "OP_PHUONGTRANG", "phuongtrang", "123456", "Đại diện FUTA", "19006067",
                "Nhà xe Phương Trang (FUTA)", "19006067", "Bến xe Miền Đông, TP. Hồ Chí Minh"
        );
        ctx.getUserRepository().save(opPhuongTrang);

        // 2. Tạo tài khoản Khách hàng mẫu
        Customer cusAn = UserFactory.createCustomer(
                "CUS_AN", "khachhang", "123456", "Nguyễn Văn An", "0912000111", 
                "an.nguyen@gmail.com", "Nam Định"
        );
        ctx.getUserRepository().save(cusAn);

        Customer cusHoang = UserFactory.createCustomer(
                "CUS_HOANG", "minhhoang", "123456", "Lê Minh Hoàng", "0983000222", 
                "hoang.le@gmail.com", "Nghệ An"
        );
        ctx.getUserRepository().save(cusHoang);

        // 3. Tạo đội xe cho Nhà xe Hoàng Long
        Vehicle v1 = VehicleFactory.createVehicle(VehicleType.LIMOUSINE, "VEH_HL01", "29B-123.45", 
                opHoangLong.getId(), 24, "Bến xe Giáp Bát - Hà Nội");
        v1.setStatus(VehicleStatus.AT_STATION);
        ctx.getVehicleRepository().save(v1);

        Vehicle v2 = VehicleFactory.createVehicle(VehicleType.SLEEPER, "VEH_HL02", "29B-888.88", 
                opHoangLong.getId(), 40, "Bến xe Nước Ngầm - Hà Nội");
        v2.setStatus(VehicleStatus.AT_STATION);
        ctx.getVehicleRepository().save(v2);

        Vehicle v3 = VehicleFactory.createVehicle(VehicleType.SEATER, "VEH_HL03", "29B-999.99", 
                opHoangLong.getId(), 29, "Bến xe Giáp Bát - Hà Nội");
        v3.setStatus(VehicleStatus.IN_TRANSIT);
        ctx.getVehicleRepository().save(v3);

        // Tạo đội xe cho Nhà xe Phương Trang
        Vehicle v4 = VehicleFactory.createVehicle(VehicleType.SLEEPER, "VEH_PT01", "51B-112.23", 
                opPhuongTrang.getId(), 36, "Bến xe Miền Đông - TP.HCM");
        v4.setStatus(VehicleStatus.AT_STATION);
        ctx.getVehicleRepository().save(v4);

        Vehicle v5 = VehicleFactory.createVehicle(VehicleType.LIMOUSINE, "VEH_PT02", "51B-456.78", 
                opPhuongTrang.getId(), 22, "Bến xe Miền Tây - TP.HCM");
        v5.setStatus(VehicleStatus.AT_STATION);
        ctx.getVehicleRepository().save(v5);

        // 4. Tạo các tài xế phụ trách chuyến
        Driver d1 = new Driver("DRV_01", "Nguyễn Văn Hùng", "0912345678", "GPLX-E-019283", 10);
        Driver d2 = new Driver("DRV_02", "Trần Văn Dũng", "0987654321", "GPLX-E-098712", 12);
        Driver d3 = new Driver("DRV_03", "Lê Văn Thành", "0905123456", "GPLX-E-554433", 15);
        Driver d4 = new Driver("DRV_04", "Phạm Quốc Bảo", "0933445566", "GPLX-E-776655", 14);
        Driver d5 = new Driver("DRV_05", "Võ Minh Trí", "0977889900", "GPLX-D-112233", 8);

        // 5. Tạo các chuyến xe về quê (Hôm nay và ngày mai)
        LocalDate today = LocalDate.now();

        // Tuyến Hà Nội -> Nam Định
        Trip t1 = new Trip("TRIP_HN_ND_01", opHoangLong.getId(), opHoangLong.getCompanyName(),
                v1.getId(), v1.getLicensePlate(), v1.getVehicleType(), d1,
                "Hà Nội (Bến xe Giáp Bát)", "Nam Định (Bến xe Nam Định)",
                LocalDateTime.of(today, LocalTime.of(7, 30)), 1.5, 120000, 24);
        t1.setAvailableSeats(22);
        ctx.getTripRepository().save(t1);

        Trip t1_afternoon = new Trip("TRIP_HN_ND_02", opHoangLong.getId(), opHoangLong.getCompanyName(),
                v1.getId(), v1.getLicensePlate(), v1.getVehicleType(), d1,
                "Hà Nội (Bến xe Giáp Bát)", "Nam Định (Bến xe Nam Định)",
                LocalDateTime.of(today, LocalTime.of(14, 0)), 1.5, 120000, 24);
        t1_afternoon.setAvailableSeats(24);
        ctx.getTripRepository().save(t1_afternoon);

        // Tuyến Hà Nội -> Thanh Hóa
        Trip t2 = new Trip("TRIP_HN_TH_01", opHoangLong.getId(), opHoangLong.getCompanyName(),
                v2.getId(), v2.getLicensePlate(), v2.getVehicleType(), d2,
                "Hà Nội (Bến xe Nước Ngầm)", "Thanh Hóa (Bến xe Phía Bắc TP. Thanh Hóa)",
                LocalDateTime.of(today, LocalTime.of(9, 0)), 2.5, 180000, 40);
        t2.setAvailableSeats(38);
        ctx.getTripRepository().save(t2);

        // Tuyến Hà Nội -> Nghệ An (Chuyến đêm về quê)
        Trip t3 = new Trip("TRIP_HN_NA_01", opHoangLong.getId(), opHoangLong.getCompanyName(),
                v2.getId(), v2.getLicensePlate(), v2.getVehicleType(), d3,
                "Hà Nội (Bến xe Nước Ngầm)", "Nghệ An (Bến xe Bắc Vinh)",
                LocalDateTime.of(today, LocalTime.of(21, 0)), 5.5, 250000, 40);
        t3.setAvailableSeats(35);
        ctx.getTripRepository().save(t3);

        // Tuyến TP.HCM -> Đà Lạt
        Trip t4 = new Trip("TRIP_HCM_DL_01", opPhuongTrang.getId(), opPhuongTrang.getCompanyName(),
                v4.getId(), v4.getLicensePlate(), v4.getVehicleType(), d4,
                "TP. Hồ Chí Minh (Bến xe Miền Đông)", "Lâm Đồng (Bến xe Liên tỉnh Đà Lạt)",
                LocalDateTime.of(today, LocalTime.of(22, 30)), 7.0, 300000, 36);
        t4.setAvailableSeats(34);
        ctx.getTripRepository().save(t4);

        // Tuyến TP.HCM -> Cần Thơ
        Trip t5 = new Trip("TRIP_HCM_CT_01", opPhuongTrang.getId(), opPhuongTrang.getCompanyName(),
                v5.getId(), v5.getLicensePlate(), v5.getVehicleType(), d5,
                "TP. Hồ Chí Minh (Bến xe Miền Tây)", "Cần Thơ (Bến xe Trung tâm Cần Thơ)",
                LocalDateTime.of(today, LocalTime.of(15, 30)), 3.0, 165000, 22);
        t5.setAvailableSeats(20);
        ctx.getTripRepository().save(t5);

        // 6. Tạo đặt vé mẫu cho khách hàng Nguyễn Văn An
        Booking b1 = new Booking("BOOK_DEMO_01", t1.getId(), cusAn.getId(), cusAn.getFullName(), 
                cusAn.getPhoneNumber(), 2, 240000, "Cổng Bến xe Giáp Bát");
        b1.setStatus(BookingStatus.CONFIRMED);
        ctx.getBookingRepository().save(b1);

        // 7. Tạo đánh giá mẫu
        Review r1 = new Review("REV_01", opHoangLong.getId(), t1.getId(), cusAn.getId(), 
                cusAn.getFullName(), 5, "Xe chạy êm ái, bác tài Hùng lái rất cẩn thận và đúng giờ. Rất hài lòng!");
        ctx.getReviewRepository().save(r1);

        Review r2 = new Review("REV_02", opHoangLong.getId(), t2.getId(), cusHoang.getId(), 
                cusHoang.getFullName(), 4, "Chất lượng xe tốt, điều hòa mát rượi, nhân viên phục vụ tận tình.");
        ctx.getReviewRepository().save(r2);

        System.out.println("Khoi tao du lieu mau thanh cong!");
    }
}
