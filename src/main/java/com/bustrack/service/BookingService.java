package com.bustrack.service;

import com.bustrack.model.booking.Booking;
import com.bustrack.model.booking.BookingStatus;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.bustrack.repository.BookingRepository;
import com.bustrack.repository.TripRepository;
import com.bustrack.strategy.IPricingStrategy;
import com.bustrack.strategy.StandardPricingStrategy;
import com.bustrack.util.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lớp cài đặt dịch vụ Đặt vé xe của khách hàng
 */
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;

    public BookingService(BookingRepository bookingRepository, TripRepository tripRepository) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public Booking bookTicket(String tripId, String customerId, String customerName, 
                             String customerPhone, int seatCount, String pickupPoint, 
                             IPricingStrategy pricingStrategy) {
        if (!ValidationUtils.isNotEmpty(tripId)) {
            throw new IllegalArgumentException("Chuyến xe không hợp lệ!");
        }
        if (seatCount <= 0) {
            throw new IllegalArgumentException("Số lượng ghế đặt phải lớn hơn 0!");
        }

        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thông tin chuyến xe!");
        }

        Trip trip = tripOpt.get();
        if (trip.getStatus() == TripStatus.CANCELLED) {
            throw new IllegalStateException("Chuyến xe này đã bị hủy!");
        }
        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new IllegalStateException("Chuyến xe này đã hoàn thành!");
        }
        if (trip.getAvailableSeats() < seatCount) {
            throw new IllegalStateException("Chuyến xe chỉ còn " + trip.getAvailableSeats() + " ghế trống!");
        }

        // Tính giá vé thông qua Strategy Pattern (Polymorphism)
        IPricingStrategy strategy = pricingStrategy != null ? pricingStrategy : new StandardPricingStrategy();
        double totalPrice = strategy.calculatePrice(trip, seatCount);

        // Trừ số ghế khả dụng của chuyến
        trip.bookSeats(seatCount);
        tripRepository.update(trip);

        String id = "BOOK_" + UUID.randomUUID().toString().substring(0, 8);
        Booking booking = new Booking(id, tripId, customerId, customerName, customerPhone, 
                seatCount, totalPrice, pickupPoint != null ? pickupPoint.trim() : trip.getDepartureLocation());

        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isPresent()) {
            Booking booking = opt.get();
            // Hoàn lại ghế cho chuyến xe nếu vé chưa bị hủy
            if (booking.getStatus() != BookingStatus.CANCELLED) {
                tripRepository.findById(booking.getTripId()).ifPresent(trip -> {
                    trip.releaseSeats(booking.getSeatCount());
                    tripRepository.update(trip);
                });
            }

            // Xóa trực tiếp vé đã hủy khỏi danh sách lưu trữ
            return bookingRepository.deleteById(bookingId);
        }
        return false;
    }

    @Override
    public boolean deleteBooking(String bookingId) {
        return bookingRepository.deleteById(bookingId);
    }

    @Override
    public int deleteCancelledBookingsByCustomer(String customerId) {
        if (customerId == null) return 0;
        List<Booking> list = bookingRepository.findByCustomerId(customerId);
        int count = 0;
        for (Booking b : list) {
            if (b.getStatus() == BookingStatus.CANCELLED) {
                bookingRepository.deleteById(b.getId());
                count++;
            }
        }
        return count;
    }

    @Override
    public Optional<Booking> getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public List<Booking> getBookingsByCustomer(String customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsByTrip(String tripId) {
        return bookingRepository.findByTripId(tripId);
    }
}
