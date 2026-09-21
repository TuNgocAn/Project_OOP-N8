package com.bustrack.ui.customer;

import com.bustrack.model.booking.Booking;
import com.bustrack.model.booking.BookingStatus;
import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.bustrack.model.user.Customer;
import com.bustrack.service.IAuthService;
import com.bustrack.service.IBookingService;
import com.bustrack.service.IReviewService;
import com.bustrack.service.ITripService;
import com.bustrack.strategy.HolidayPricingStrategy;
import com.bustrack.strategy.IPricingStrategy;
import com.bustrack.strategy.StandardPricingStrategy;
import com.bustrack.strategy.StudentDiscountPricingStrategy;
import com.bustrack.ui.LoginFrame;
import com.bustrack.ui.UITheme;
import com.bustrack.util.AppContext;
import com.bustrack.util.DateTimeUtils;
import com.bustrack.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Màn hình làm việc chính dành cho Khách hàng
 * Đáp ứng đầy đủ các yêu cầu:
 * - Thông tin cá nhân của khách hàng
 * - Chặng đường mong muốn (tìm xe về quê)
 * - Thời gian đi
 * - Có chuyến xe nào, giá vé bao nhiêu
 * - Đánh giá chuyến xe / nhà xe
 */
public class CustomerMainFrame extends JFrame {
    private final Customer customer;
    private final ITripService tripService = AppContext.getInstance().getTripService();
    private final IBookingService bookingService = AppContext.getInstance().getBookingService();
    private final IReviewService reviewService = AppContext.getInstance().getReviewService();
    private final IAuthService authService = AppContext.getInstance().getAuthService();

    // UI Search Components
    private JTextField txtSearchDep;
    private JTextField txtSearchDest;
    private JTextField txtSearchDate;
    private JComboBox<String> cbPricingStrategy;
    private JTable searchResultTable;
    private DefaultTableModel searchResultTableModel;

    // UI My Bookings
    private JTable myBookingsTable;
    private DefaultTableModel myBookingsTableModel;

    // UI Profile
    private JTextField txtProfileName;
    private JTextField txtProfilePhone;
    private JTextField txtProfileEmail;
    private JTextField txtProfileHometown;
    private JLabel lblHeaderGreeting;

    public CustomerMainFrame(Customer customer) {
        super("BusTrack - Tra Cứu Giờ Xe & Đặt Vé Về Quê");
        this.customer = customer;
        initUI();
        performSearch(false);
        refreshMyBookings();
    }

    private void initUI() {
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Top Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("BUSTRACK - THEO DÕI GIỜ XE VỀ QUÊ");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        String hometownStr = (customer.getHometown() != null && !customer.getHometown().isBlank()) ? customer.getHometown() : "Chưa cập nhật";
        lblHeaderGreeting = new JLabel("Xin chào: " + customer.getFullName() + " | Quê quán: " + hometownStr + " | SĐT: " + customer.getPhoneNumber());
        lblHeaderGreeting.setFont(UITheme.FONT_REGULAR);
        lblHeaderGreeting.setForeground(new Color(230, 240, 255));

        titlePanel.add(lblTitle);
        titlePanel.add(lblHeaderGreeting);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel userActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userActions.setOpaque(false);
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(UITheme.FONT_BOLD);
        btnLogout.setBackground(new Color(255, 255, 255, 220));
        btnLogout.setForeground(UITheme.PRIMARY);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> logout());
        userActions.add(btnLogout);
        headerPanel.add(userActions, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Center Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BOLD);
        tabbedPane.addTab("  Tra Cứu Chuyến Xe Về Quê & Đặt Vé  ", createSearchPanel());
        tabbedPane.addTab("  Chuyến Xe Của Tôi (Vé Đã Đặt)  ", createMyBookingsPanel());
        tabbedPane.addTab("  Thông Tin Cá Nhân  ", createProfilePanel());

        // Tự động làm mới dữ liệu khi người dùng chuyển sang tab "Chuyến Xe Của Tôi"
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                refreshMyBookings(false);
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ================= TAB 1: TÌM XE & ĐẶT VÉ =================
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        panel.setBackground(UITheme.BG_LIGHT);

        // Search Form Box
        JPanel searchBox = new JPanel(new GridBagLayout());
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 6, 4, 6);

        // Nơi xuất phát
        gbc.gridx = 0; gbc.gridy = 0;
        searchBox.add(new JLabel("Nơi đi / Bến xuất phát:"), gbc);
        gbc.gridx = 1;
        txtSearchDep = new JTextField("Hà Nội", 12);
        searchBox.add(txtSearchDep, gbc);

        // Nơi đến (Về quê)
        gbc.gridx = 2;
        searchBox.add(new JLabel("Chặng về quê (Điểm đến):"), gbc);
        gbc.gridx = 3;
        txtSearchDest = new JTextField(customer.getHometown() != null ? customer.getHometown() : "Nam Định", 12);
        searchBox.add(txtDestQuickHelper(txtSearchDest), gbc);

        // Ngày đi
        gbc.gridx = 4;
        searchBox.add(new JLabel("Ngày đi (dd/MM/yyyy):"), gbc);
        gbc.gridx = 5;
        txtSearchDate = new JTextField(DateTimeUtils.formatDate(LocalDate.now()), 9);
        searchBox.add(txtSearchDate, gbc);

        // Chế độ ưu đãi giá (Strategy Pattern UI)
        gbc.gridx = 0; gbc.gridy = 1;
        searchBox.add(new JLabel("Chính sách giá vé:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cbPricingStrategy = new JComboBox<>(new String[]{
                "Giá ngày thường tiêu chuẩn",
                "Ưu đãi sinh viên về quê (Giảm 10%)",
                "Cao điểm Lễ / Tết (Phụ thu 20%)"
        });
        cbPricingStrategy.addActionListener(e -> performSearch(true));
        searchBox.add(cbPricingStrategy, gbc);
        gbc.gridwidth = 1;

        // Search Buttons
        gbc.gridx = 4; gbc.gridy = 1;
        JButton btnSearch = UITheme.createPrimaryButton("🔍 Tìm Chuyến Xe");
        btnSearch.addActionListener(e -> performSearch(true));
        searchBox.add(btnSearch, gbc);

        gbc.gridx = 5;
        JButton btnShowAll = new JButton("Xem tất cả chuyến");
        btnShowAll.addActionListener(e -> performSearch(false));
        searchBox.add(btnShowAll, gbc);

        panel.add(searchBox, BorderLayout.NORTH);

        // Search Results Table
        JPanel tableContainer = new JPanel(new BorderLayout(5, 5));
        tableContainer.setOpaque(false);

        String[] columns = {
                "Mã Chuyến", "Nhà Xe", "Điểm Xuất Phát", "Chặng Về Quê (Đến)", 
                "Giờ Xuất Bến", "Thời Gian Đi", "Giờ Đến Dự Kiến", "Loại Xe", "Biển Số", 
                "Tài Xế & SĐT", "Giá Vé (Áp dụng)", "Ghế Còn", "Trạng Thái"
        };
        searchResultTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        searchResultTable = new JTable(searchResultTableModel);
        UITheme.styleTable(searchResultTable);

        tableContainer.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);

        // Booking Action Bar
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actionBar.setOpaque(false);

        JButton btnBook = UITheme.createSuccessButton("🎟️ Đặt Chỗ Chuyến Xe Đã Chọn");
        btnBook.addActionListener(e -> openBookTicketDialog());
        actionBar.add(btnBook);

        tableContainer.add(actionBar, BorderLayout.SOUTH);
        panel.add(tableContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel txtDestQuickHelper(JTextField target) {
        JPanel p = new JPanel(new BorderLayout(3, 0));
        p.setOpaque(false);
        p.add(target, BorderLayout.CENTER);
        JButton btnMyTown = new JButton("Quê tôi");
        btnMyTown.setToolTipText("Điền nhanh quê quán của bạn");
        btnMyTown.setFont(UITheme.FONT_SMALL);
        btnMyTown.addActionListener(e -> {
            if (customer.getHometown() != null && !customer.getHometown().isBlank()) {
                target.setText(customer.getHometown());
            }
        });
        p.add(btnMyTown, BorderLayout.EAST);
        return p;
    }

    // ================= TAB 2: VÉ ĐÃ ĐẶT =================
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        panel.setBackground(UITheme.BG_LIGHT);

        String[] columns = {
                "Mã Vé", "Mã Chuyến", "Chặng Đi", "Giờ Xuất Bến", "Thời Gian Đi", 
                "Giờ Đến Dự Kiến", "Nhà Xe", "Tài Xế & SĐT", "Số Vé", 
                "Tổng Tiền", "Điểm Đón", "Trạng Thái Xe", "Trạng Thái Vé"
        };
        myBookingsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myBookingsTable = new JTable(myBookingsTableModel);
        UITheme.styleTable(myBookingsTable);

        panel.add(new JScrollPane(myBookingsTable), BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        toolbar.setOpaque(false);

        JButton btnReview = new JButton("⭐ Đánh Giá Chuyến Đi / Nhà Xe");
        btnReview.setFont(UITheme.FONT_BOLD);
        btnReview.setBackground(new Color(241, 196, 15));
        btnReview.setForeground(Color.BLACK);
        btnReview.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReview.addActionListener(e -> openReviewDialog());

        JButton btnCancel = UITheme.createDangerButton("Hủy Vé");
        btnCancel.addActionListener(e -> cancelSelectedBooking());

        JButton btnDeleteCancelled = new JButton("🗑️ Xóa Vé Đã Hủy");
        btnDeleteCancelled.setFont(UITheme.FONT_BOLD);
        btnDeleteCancelled.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteCancelled.setToolTipText("Xóa toàn bộ các vé đã hủy khỏi lịch sử của bạn");
        btnDeleteCancelled.addActionListener(e -> deleteCancelledBookingsAction());

        JButton btnRefresh = new JButton("🔄 Làm mới danh sách");
        btnRefresh.setFont(UITheme.FONT_BOLD);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshMyBookings(true));

        toolbar.add(btnReview);
        toolbar.add(btnCancel);
        toolbar.add(btnDeleteCancelled);
        toolbar.add(btnRefresh);

        panel.add(toolbar, BorderLayout.SOUTH);

        return panel;
    }

    // ================= TAB 3: THÔNG TIN CÁ NHÂN =================
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(UITheme.BG_LIGHT);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JLabel lblUsername = new JLabel(customer.getUsername());
        lblUsername.setFont(UITheme.FONT_BOLD);
        form.add(lblUsername, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        txtProfileName = new JTextField(customer.getFullName());
        form.add(txtProfileName, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        txtProfilePhone = new JTextField(customer.getPhoneNumber());
        form.add(txtProfilePhone, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Email liên hệ:"), gbc);
        gbc.gridx = 1;
        txtProfileEmail = new JTextField(customer.getEmail());
        form.add(txtProfileEmail, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Quê quán (Nơi thường về):"), gbc);
        gbc.gridx = 1;
        txtProfileHometown = new JTextField(customer.getHometown());
        form.add(txtProfileHometown, gbc);
        row++;

        gbc.gridx = 1; gbc.gridy = row;
        JButton btnSave = UITheme.createPrimaryButton("Cập nhật thông tin");
        btnSave.addActionListener(e -> updateProfile());
        form.add(btnSave, gbc);

        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    // ================= LOGIC TÌM KIẾM & ĐẶT VÉ =================
    private IPricingStrategy getSelectedPricingStrategy() {
        int idx = cbPricingStrategy.getSelectedIndex();
        if (idx == 1) return new StudentDiscountPricingStrategy();
        if (idx == 2) return new HolidayPricingStrategy();
        return new StandardPricingStrategy();
    }

    private void performSearch(boolean filterByInput) {
        String dep = filterByInput ? txtSearchDep.getText().trim() : "";
        String dest = filterByInput ? txtSearchDest.getText().trim() : "";
        LocalDate date = null;

        if (filterByInput && !txtSearchDate.getText().isBlank()) {
            date = DateTimeUtils.parseDate(txtSearchDate.getText().trim());
        }

        List<Trip> trips = tripService.searchTrips(dep, dest, date);
        searchResultTableModel.setRowCount(0);

        IPricingStrategy strategy = getSelectedPricingStrategy();

        for (Trip t : trips) {
            double effectivePrice = strategy.calculatePrice(t, 1);
            String driverText = t.getDriver() != null ? t.getDriver().getFullName() + " (" + t.getDriver().getPhoneNumber() + ")" : "Chưa cập nhật";

            searchResultTableModel.addRow(new Object[]{
                    t.getId(),
                    t.getOperatorName(),
                    t.getDepartureLocation(),
                    t.getDestinationLocation(),
                    DateTimeUtils.formatDateTime(t.getDepartureTime()),
                    t.getEstimatedDurationHours() + " giờ",
                    DateTimeUtils.formatDateTime(t.getEstimatedArrivalTime()),
                    t.getVehicleType().getDisplayName(),
                    t.getLicensePlate(),
                    driverText,
                    String.format("%,.0f VNĐ", effectivePrice),
                    t.getAvailableSeats() + " / " + t.getTotalSeats(),
                    t.getStatus().getDisplayName()
            });
        }
    }

    private void openBookTicketDialog() {
        int row = searchResultTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chuyến xe từ bảng kết quả để đặt vé!", "Chưa chọn chuyến", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tripId = (String) searchResultTableModel.getValueAt(row, 0);
        var tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty()) return;

        Trip trip = tripOpt.get();
        if (trip.getAvailableSeats() <= 0) {
            JOptionPane.showMessageDialog(this, "Rất tiếc, chuyến xe này đã hết ghế trống!", "Hết vé", JOptionPane.WARNING_MESSAGE);
            return;
        }

        IPricingStrategy strategy = getSelectedPricingStrategy();

        JDialog dialog = new JDialog(this, "Đặt Vé Chuyến: " + trip.getDepartureLocation() + " -> " + trip.getDestinationLocation(), true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTrip = new JLabel(trip.getDepartureLocation() + " -> " + trip.getDestinationLocation());
        lblTrip.setFont(UITheme.FONT_BOLD);

        JLabel lblTime = new JLabel(DateTimeUtils.formatDateTime(trip.getDepartureTime()));
        lblTime.setFont(UITheme.FONT_BOLD);

        JSpinner spSeatCount = new JSpinner(new SpinnerNumberModel(1, 1, Math.min(10, trip.getAvailableSeats()), 1));
        JTextField txtPickup = new JTextField(trip.getDepartureLocation());

        JLabel lblTotalPrice = new JLabel(String.format("%,.0f VNĐ", strategy.calculatePrice(trip, 1)));
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalPrice.setForeground(UITheme.DANGER);

        spSeatCount.addChangeListener(e -> {
            int seats = (int) spSeatCount.getValue();
            lblTotalPrice.setText(String.format("%,.0f VNĐ", strategy.calculatePrice(trip, seats)));
        });

        form.add(new JLabel("Chặng đường:")); form.add(lblTrip);
        form.add(new JLabel("Giờ xuất bến:")); form.add(lblTime);
        form.add(new JLabel("Chính sách áp dụng:")); form.add(new JLabel(strategy.getStrategyName()));
        form.add(new JLabel("Số lượng ghế đặt:")); form.add(spSeatCount);
        form.add(new JLabel("Điểm đón cụ thể:")); form.add(txtPickup);
        form.add(new JLabel("Tổng tiền thanh toán:")); form.add(lblTotalPrice);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnConfirm = UITheme.createSuccessButton("Xác Nhận Đặt Vé");
        btnConfirm.addActionListener(e -> {
            try {
                int seats = (int) spSeatCount.getValue();
                String pickup = txtPickup.getText().trim();

                bookingService.bookTicket(trip.getId(), customer.getId(), customer.getFullName(), 
                        customer.getPhoneNumber(), seats, pickup, strategy);

                JOptionPane.showMessageDialog(dialog, "Đặt vé thành công!\nBạn có thể theo dõi giờ xe và tài xế trong mục 'Chuyến xe của tôi'.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                performSearch(true);
                refreshMyBookings();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Đặt vé thất bại", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnCancel);
        btnPanel.add(btnConfirm);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void refreshMyBookings() {
        refreshMyBookings(false);
    }

    private void refreshMyBookings(boolean showNotification) {
        try {
            // Nạp lại dữ liệu từ tệp tin JSON để đảm bảo cập nhật trạng thái mới nhất từ nhà xe
            AppContext.getInstance().getBookingRepository().reload();
            AppContext.getInstance().getTripRepository().reload();

            myBookingsTableModel.setRowCount(0);
            List<Booking> bookings = bookingService.getBookingsByCustomer(customer.getId());

            for (Booking b : bookings) {
                if (b == null) continue;

                var tripOpt = tripService.getTripById(b.getTripId());
                String route = "Không xác định";
                String depTime = "-";
                String durationText = "-";
                String arrTime = "-";
                String opName = "-";
                String driverText = "Chưa gán tài xế";
                String tripStatusText = "Không xác định";

                if (tripOpt.isPresent()) {
                    Trip t = tripOpt.get();
                    route = t.getDepartureLocation() + " ➔ " + t.getDestinationLocation();
                    depTime = DateTimeUtils.formatDateTime(t.getDepartureTime());
                    durationText = t.getEstimatedDurationHours() + " giờ";
                    arrTime = DateTimeUtils.formatDateTime(t.getEstimatedArrivalTime());
                    opName = t.getOperatorName();
                    driverText = (t.getDriver() != null && t.getDriver().getFullName() != null)
                            ? t.getDriver().getFullName() + " (" + (t.getDriver().getPhoneNumber() != null ? t.getDriver().getPhoneNumber() : "Chưa có SĐT") + ")"
                            : "Chưa gán tài xế";
                    tripStatusText = t.getStatus() != null ? t.getStatus().getDisplayName() : "Đang chờ";
                }

                String bookingStatusText = b.getStatus() != null ? b.getStatus().getDisplayName() : "Đã xác nhận";

                myBookingsTableModel.addRow(new Object[]{
                        b.getId(),
                        b.getTripId() != null ? b.getTripId() : "-",
                        route,
                        depTime,
                        durationText,
                        arrTime,
                        opName,
                        driverText,
                        b.getSeatCount() + " vé",
                        String.format("%,.0f VNĐ", b.getTotalPrice()),
                        b.getPickupPoint() != null ? b.getPickupPoint() : "-",
                        tripStatusText,
                        bookingStatusText
                });
            }

            if (showNotification) {
                JOptionPane.showMessageDialog(this, 
                        "Đã làm mới danh sách chuyến xe thành công!\nHiện có " + bookings.size() + " vé trong danh sách của bạn.", 
                        "Làm mới thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi làm mới danh sách chuyến xe: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelSelectedBooking() {
        int row = myBookingsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một vé từ danh sách để hủy!", "Chưa chọn vé", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) myBookingsTableModel.getValueAt(row, 0);
        var bookingOpt = bookingService.getBookingById(bookingId);
        if (bookingOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin vé này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = bookingOpt.get();
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            int delConfirm = JOptionPane.showConfirmDialog(this, 
                    "Vé này đã ở trạng thái hủy. Bạn có muốn xóa hẳn mục vé này khỏi danh sách không?", 
                    "Xóa vé đã hủy", JOptionPane.YES_NO_OPTION);
            if (delConfirm == JOptionPane.YES_OPTION) {
                bookingService.deleteBooking(bookingId);
                JOptionPane.showMessageDialog(this, "Đã xóa mục vé đã hủy khỏi danh sách!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                refreshMyBookings(false);
            }
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn hủy vé " + bookingId + " không?\nSố chỗ ngồi sẽ được hoàn trả lại cho chuyến xe và vé sẽ được xóa khỏi danh sách.", 
                "Xác nhận hủy vé", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookingService.cancelBooking(bookingId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã hủy và xóa vé thành công! Chỗ trống đã được hoàn trả cho chuyến xe.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                refreshMyBookings(false);
                performSearch(true);
            } else {
                JOptionPane.showMessageDialog(this, "Hủy vé thất bại! Vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCancelledBookingsAction() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa toàn bộ các mục vé đã hủy khỏi danh sách của bạn?", 
                "Xác nhận xóa vé đã hủy", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int count = bookingService.deleteCancelledBookingsByCustomer(customer.getId());
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công " + count + " vé đã hủy khỏi danh sách của bạn!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không có vé đã hủy nào trong danh sách.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshMyBookings(false);
        }
    }

    private void openReviewDialog() {
        int row = myBookingsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chuyến xe trong danh sách vé đã đặt để đánh giá!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) myBookingsTableModel.getValueAt(row, 0);
        var bookingOpt = bookingService.getBookingById(bookingId);
        if (bookingOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin vé đã chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = bookingOpt.get();
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            JOptionPane.showMessageDialog(this, "Không thể đánh giá chuyến xe cho vé đã bị hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        var tripOpt = tripService.getTripById(booking.getTripId());
        if (tripOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chuyến xe liên kết với vé này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Trip trip = tripOpt.get();

        JDialog dialog = new JDialog(this, "Đánh Giá Chất Lượng: " + trip.getOperatorName(), true);
        dialog.setSize(440, 340);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new BorderLayout(8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JPanel top = new JPanel(new GridLayout(2, 2, 5, 5));
        top.add(new JLabel("Chuyến đi:"));
        top.add(new JLabel(trip.getDepartureLocation() + " ➔ " + trip.getDestinationLocation()));
        top.add(new JLabel("Chấm điểm sao:"));
        JComboBox<String> cbRating = new JComboBox<>(new String[]{
                "⭐⭐⭐⭐⭐ (5 sao - Xuất sắc)",
                "⭐⭐⭐⭐ (4 sao - Rất tốt)",
                "⭐⭐⭐ (3 sao - Tạm ổn)",
                "⭐⭐ (2 sao - Cần cải thiện)",
                "⭐ (1 sao - Kém)"
        });
        top.add(cbRating);
        form.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.add(new JLabel("Lời nhận xét / Góp ý:"), BorderLayout.NORTH);
        JTextArea txtComment = new JTextArea(4, 20);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        txtComment.setText("Xe chạy đúng giờ, tài xế lái cẩn thận và nhiệt tình.");
        center.add(new JScrollPane(txtComment), BorderLayout.CENTER);
        form.add(center, BorderLayout.CENTER);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = new JButton("Đóng");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSubmit = UITheme.createPrimaryButton("Gửi Đánh Giá");
        btnSubmit.addActionListener(e -> {
            int rating = 5 - cbRating.getSelectedIndex();
            String comment = txtComment.getText().trim();
            reviewService.addReview(trip.getOperatorId(), trip.getId(), customer.getId(), customer.getFullName(), rating, comment);
            JOptionPane.showMessageDialog(dialog, "Cảm ơn bạn đã gửi đánh giá! Ý kiến của bạn sẽ giúp nhà xe nâng cao chất lượng phục vụ.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        btnPanel.add(btnCancel);
        btnPanel.add(btnSubmit);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void updateProfile() {
        String name = txtProfileName.getText().trim();
        String phone = txtProfilePhone.getText().trim();
        String email = txtProfileEmail.getText().trim();
        String hometown = txtProfileHometown.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên và số điện thoại không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = authService.updateCustomerProfile(customer.getId(), name, phone, email, hometown);
        if (ok) {
            customer.setFullName(name);
            customer.setPhoneNumber(phone);
            customer.setEmail(email);
            customer.setHometown(hometown);

            lblHeaderGreeting.setText("Xin chào: " + customer.getFullName() + " | Quê quán: " + (hometown.isEmpty() ? "Chưa cập nhật" : hometown) + " | SĐT: " + phone);
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin cá nhân thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
        new LoginFrame().setVisible(true);
        dispose();
    }
}
