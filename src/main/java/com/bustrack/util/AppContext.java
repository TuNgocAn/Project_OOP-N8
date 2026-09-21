package com.bustrack.util;

import com.bustrack.repository.*;
import com.bustrack.service.*;

/**
 * Lớp trung tâm quản lý cấu hình và khởi tạo các Service, Repository (OOP: Singleton, Service Locator)
 */
public class AppContext {
    private static volatile AppContext instance;

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    private final IAuthService authService;
    private final IVehicleService vehicleService;
    private final ITripService tripService;
    private final IBookingService bookingService;
    private final IReviewService reviewService;

    private AppContext() {
        // Khởi tạo các Repository
        this.userRepository = new UserRepository();
        this.vehicleRepository = new VehicleRepository();
        this.tripRepository = new TripRepository();
        this.bookingRepository = new BookingRepository();
        this.reviewRepository = new ReviewRepository();

        // Khởi tạo các Service
        this.authService = new AuthService(userRepository);
        this.vehicleService = new VehicleService(vehicleRepository);
        this.tripService = new TripService(tripRepository, vehicleRepository);
        this.bookingService = new BookingService(bookingRepository, tripRepository);
        this.reviewService = new ReviewService(reviewRepository);
    }

    public static AppContext getInstance() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public VehicleRepository getVehicleRepository() {
        return vehicleRepository;
    }

    public TripRepository getTripRepository() {
        return tripRepository;
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    public IAuthService getAuthService() {
        return authService;
    }

    public IVehicleService getVehicleService() {
        return vehicleService;
    }

    public ITripService getTripService() {
        return tripService;
    }

    public IBookingService getBookingService() {
        return bookingService;
    }

    public IReviewService getReviewService() {
        return reviewService;
    }
}
