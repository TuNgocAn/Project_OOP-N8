package com.bustrack;

import com.bustrack.model.booking.Booking;
import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.bustrack.model.vehicle.VehicleType;
import com.bustrack.repository.BookingRepository;
import com.bustrack.repository.TripRepository;
import com.bustrack.repository.VehicleRepository;
import com.bustrack.service.BookingService;
import com.bustrack.service.TripService;
import com.bustrack.strategy.StandardPricingStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kiểm thử nghiệp vụ Chuyến xe và Đặt vé (Trip & Booking Service Test)
 */
public class TripServiceTest {
    private static final String TEST_TRIPS = "data/test_trips.json";
    private static final String TEST_BOOKINGS = "data/test_bookings.json";
    private static final String TEST_VEHICLES = "data/test_vehicles.json";

    private TripRepository tripRepo;
    private BookingRepository bookingRepo;
    private VehicleRepository vehicleRepo;
    private TripService tripService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        cleanFiles();
        tripRepo = new TripRepository(TEST_TRIPS);
        bookingRepo = new BookingRepository(TEST_BOOKINGS);
        vehicleRepo = new VehicleRepository(TEST_VEHICLES);
        tripService = new TripService(tripRepo, vehicleRepo);
        bookingService = new BookingService(bookingRepo, tripRepo);
    }

    @AfterEach
    void tearDown() {
        cleanFiles();
    }

    private void cleanFiles() {
        new File(TEST_TRIPS).delete();
        new File(TEST_BOOKINGS).delete();
        new File(TEST_VEHICLES).delete();
    }

    @Test
    @DisplayName("Thêm chuyến xe mới và gán tài xế thành công")
    void testCreateTripAndAssignDriver() {
        Driver driver = new Driver("D01", "Trần Văn Dũng", "0987654321", "GPLX-E-9999", 8);
        LocalDateTime departureTime = LocalDateTime.now().plusDays(1);

        Trip trip = tripService.createTrip("OP_01", "Nhà xe Hoàng Long", "V01", "29B-999.99",
                VehicleType.SLEEPER, driver, "Hà Nội", "Nghệ An", departureTime, 5.0, 250000.0, 40);

        assertNotNull(trip);
        assertEquals("Nghệ An", trip.getDestinationLocation());
        assertEquals(40, trip.getAvailableSeats());
        assertEquals("Trần Văn Dũng", trip.getDriver().getFullName());

        // Gán tài xế mới
        Driver newDriver = new Driver("D02", "Lê Văn Thành", "0905111222", "GPLX-E-8888", 12);
        boolean assigned = tripService.assignDriver(trip.getId(), newDriver);
        assertTrue(assigned);

        Trip updatedTrip = tripService.getTripById(trip.getId()).orElseThrow();
        assertEquals("Lê Văn Thành", updatedTrip.getDriver().getFullName());
    }

    @Test
    @DisplayName("Tìm kiếm chuyến xe theo chặng đường và thời gian")
    void testSearchTrips() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime depTime = LocalDateTime.of(tomorrow, java.time.LocalTime.of(8, 0));

        tripService.createTrip("OP_01", "Hoàng Long", "V01", "29B-111.11",
                VehicleType.SEATER, null, "Hà Nội (Bến Giáp Bát)", "Nam Định", depTime, 1.5, 100000.0, 30);

        tripService.createTrip("OP_01", "Hoàng Long", "V02", "29B-222.22",
                VehicleType.SLEEPER, null, "Hà Nội (Bến Nước Ngầm)", "Thanh Hóa", depTime, 2.5, 150000.0, 40);

        List<Trip> searchToNamDinh = tripService.searchTrips("Hà Nội", "Nam Định", tomorrow);
        assertEquals(1, searchToNamDinh.size());
        assertEquals("Nam Định", searchToNamDinh.get(0).getDestinationLocation());

        List<Trip> searchAllFromHanoi = tripService.searchTrips("Hà Nội", "", null);
        assertEquals(2, searchAllFromHanoi.size());
    }

    @Test
    @DisplayName("Đặt vé xe và kiểm tra trừ số lượng ghế khả dụng")
    void testBookTicketAndSeatDeduction() {
        LocalDateTime depTime = LocalDateTime.now().plusDays(2);
        Trip trip = tripService.createTrip("OP_01", "Hoàng Long", "V01", "29B-111.11",
                VehicleType.SEATER, null, "Hà Nội", "Thái Bình", depTime, 2.0, 100000.0, 20);

        Booking booking = bookingService.bookTicket(trip.getId(), "CUS_01", "Nguyễn Văn A",
                "0912345678", 3, "Bến xe", new StandardPricingStrategy());

        assertNotNull(booking);
        assertEquals(300000.0, booking.getTotalPrice());

        // Ghế trống phải giảm từ 20 xuống 17
        Trip reloaded = tripService.getTripById(trip.getId()).orElseThrow();
        assertEquals(17, reloaded.getAvailableSeats());

        // Hủy vé: ghế trống được hoàn trả
        bookingService.cancelBooking(booking.getId());
        Trip afterCancel = tripService.getTripById(trip.getId()).orElseThrow();
        assertEquals(20, afterCancel.getAvailableSeats());
    }
}
