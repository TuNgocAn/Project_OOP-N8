package com.bustrack.ui.operator;

import com.bustrack.model.trip.Driver;
import com.bustrack.model.trip.Trip;
import com.bustrack.model.trip.TripStatus;
import com.bustrack.model.user.BusOperator;
import com.bustrack.model.vehicle.Vehicle;
import com.bustrack.model.vehicle.VehicleStatus;
import com.bustrack.model.vehicle.VehicleType;
import com.bustrack.service.ITripService;
import com.bustrack.service.IVehicleService;
import com.bustrack.service.IReviewService;
import com.bustrack.ui.LoginFrame;
import com.bustrack.ui.UITheme;
import com.bustrack.util.AppContext;
import com.bustrack.util.DateTimeUtils;
import com.bustrack.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Màn hình làm việc chính dành cho Nhà xe
 * Đáp ứng đầy đủ các yêu cầu:
 * - Quản lý xe đến, xe đi, số lượng xe còn ở bến
 * - Quản lý xe
 * - Quản lý tài xế cho chuyến xe
 * - Quản lý thời gian di chuyển của từng xe
 * - Thêm thông tin chuyến xe, giá vé
 */
public class OperatorMainFrame extends JFrame {
    private final BusOperator operator;
    private final IVehicleService vehicleService = AppContext.getInstance().getVehicleService();
    private final ITripService tripService = AppContext.getInstance().getTripService();
    private final IReviewService reviewService = AppContext.getInstance().getReviewService();

    // Metric Labels
    private JLabel lblTotalVehicles;
    private JLabel lblAtStationVehicles;
    private JLabel lblInTransitVehicles;
    private JLabel lblTotalTrips;
    private JLabel lblAverageRating;

    // Tables
    private JTable tripTable;
    private DefaultTableModel tripTableModel;
    private JTable vehicleTable;
    private DefaultTableModel vehicleTableModel;
    private JTable reviewTable;
    private DefaultTableModel reviewTableModel;

    public OperatorMainFrame(BusOperator operator) {
        super("BusTrack - Bảng Điều Khiển Nhà Xe: " + operator.getCompanyName());
        this.operator = operator;
        initUI();
        refreshAllData();
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
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÀ XE: " + operator.getCompanyName().toUpperCase());
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Hotline: " + operator.getHotline() + " | Địa chỉ: " + operator.getOfficeAddress() + " | Người đại diện: " + operator.getFullName());
        lblSub.setFont(UITheme.FONT_REGULAR);
        lblSub.setForeground(new Color(230, 240, 255));

        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);
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

        // 2. Center Content with Metric Cards & TabbedPane
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        centerPanel.setBackground(UITheme.BG_LIGHT);

        // Metric Cards Panel
        JPanel metricsPanel = createMetricsPanel();
        centerPanel.add(metricsPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BOLD);
        tabbedPane.addTab("  Quản Lý Chuyến Xe & Lịch Trình  ", createTripManagementPanel());
        tabbedPane.addTab("  Quản Lý Đội Xe & Xe Ở Bến  ", createVehicleManagementPanel());
        tabbedPane.addTab("  Đánh Giá Từ Khách Hàng  ", createReviewPanel());

        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 12, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 75));

        lblTotalVehicles = new JLabel("0", SwingConstants.CENTER);
        lblAtStationVehicles = new JLabel("0", SwingConstants.CENTER);
        lblInTransitVehicles = new JLabel("0", SwingConstants.CENTER);
        lblTotalTrips = new JLabel("0", SwingConstants.CENTER);
        lblAverageRating = new JLabel("5.0 ⭐", SwingConstants.CENTER);

        panel.add(createMetricCard("TỔNG SỐ XE", lblTotalVehicles, new Color(41, 128, 185)));
        panel.add(createMetricCard("XE CÒN Ở BẾN (ĐẾN)", lblAtStationVehicles, UITheme.SUCCESS));
        panel.add(createMetricCard("XE ĐANG CHẠY (ĐI)", lblInTransitVehicles, UITheme.WARNING));
        panel.add(createMetricCard("TỔNG CHUYẾN XE", lblTotalTrips, new Color(142, 68, 173)));
        panel.add(createMetricCard("ĐÁNH GIÁ TRUNG BÌNH", lblAverageRating, new Color(230, 126, 34)));

        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblCardTitle = new JLabel(title);
        lblCardTitle.setFont(UITheme.FONT_SMALL);
        lblCardTitle.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);

        card.add(lblCardTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ================= TAB 1: QUẢN LÝ CHUYẾN XE =================
    private JPanel createTripManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        // Action Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setOpaque(false);

        JButton btnAddTrip = UITheme.createPrimaryButton("+ Thêm Chuyến Xe Mới");
        btnAddTrip.addActionListener(e -> openAddTripDialog());

        JButton btnAssignDriver = new JButton("Gán / Sửa Tài Xế");
        btnAssignDriver.setFont(UITheme.FONT_BOLD);
        btnAssignDriver.addActionListener(e -> openAssignDriverDialog());

        JButton btnStartTrip = UITheme.createSuccessButton("Xe Xuất Bến (Bắt đầu chạy)");
        btnStartTrip.addActionListener(e -> updateSelectedTripStatus(TripStatus.RUNNING));

        JButton btnCompleteTrip = new JButton("Xe Đến Bến (Hoàn thành)");
        btnCompleteTrip.setFont(UITheme.FONT_BOLD);
        btnCompleteTrip.addActionListener(e -> updateSelectedTripStatus(TripStatus.COMPLETED));

        JButton btnCancelTrip = UITheme.createDangerButton("Hủy Chuyến");
        btnCancelTrip.addActionListener(e -> updateSelectedTripStatus(TripStatus.CANCELLED));

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(UITheme.FONT_BOLD);
        btnRefresh.addActionListener(e -> refreshAllData());

        toolbar.add(btnAddTrip);
        toolbar.add(btnAssignDriver);
        toolbar.add(btnStartTrip);
        toolbar.add(btnCompleteTrip);
        toolbar.add(btnCancelTrip);
        toolbar.add(btnRefresh);

        panel.add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {
                "Mã Chuyến", "Điểm Xuất Phát", "Điểm Đến (Về Quê)", "Giờ Xuất Bến", 
                "Thời Gian Đi", "Giờ Đến Nơi", "Loại Xe", "Biển Số", "Tài Xế Phụ Trách", 
                "Giá Vé (VNĐ)", "Ghế Trống", "Trạng Thái"
        };
        tripTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tripTable = new JTable(tripTableModel);
        UITheme.styleTable(tripTable);

        JScrollPane scrollPane = new JScrollPane(tripTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= TAB 2: QUẢN LÝ XE & XE Ở BẾN =================
    private JPanel createVehicleManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        // Action Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setOpaque(false);

        JButton btnAddVehicle = UITheme.createPrimaryButton("+ Thêm Xe Vào Đội");
        btnAddVehicle.addActionListener(e -> openAddVehicleDialog());

        JButton btnToStation = UITheme.createSuccessButton("Xe Đến / Ở Bến");
        btnToStation.addActionListener(e -> updateSelectedVehicleStatus(VehicleStatus.AT_STATION));

        JButton btnInTransit = new JButton("Xe Đi / Đang Chạy");
        btnInTransit.setFont(UITheme.FONT_BOLD);
        btnInTransit.addActionListener(e -> updateSelectedVehicleStatus(VehicleStatus.IN_TRANSIT));

        JButton btnMaintenance = UITheme.createDangerButton("Bảo Dưỡng");
        btnMaintenance.addActionListener(e -> updateSelectedVehicleStatus(VehicleStatus.MAINTENANCE));

        JButton btnDeleteVehicle = new JButton("Xóa Xe");
        btnDeleteVehicle.setFont(UITheme.FONT_BOLD);
        btnDeleteVehicle.addActionListener(e -> deleteSelectedVehicle());

        toolbar.add(btnAddVehicle);
        toolbar.add(btnToStation);
        toolbar.add(btnInTransit);
        toolbar.add(btnMaintenance);
        toolbar.add(btnDeleteVehicle);

        panel.add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {
                "Mã Xe", "Biển Số Xe", "Loại Xe", "Số Chỗ Ngồi", "Trạng Thái", "Bến Đỗ Hiện Tại", "Tiện Ích & Mô Tả"
        };
        vehicleTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vehicleTable = new JTable(vehicleTableModel);
        UITheme.styleTable(vehicleTable);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= TAB 3: ĐÁNH GIÁ CỦA KHÁCH HÀNG =================
    private JPanel createReviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        String[] columns = {"Mã Đánh Giá", "Mã Chuyến", "Khách Hàng", "Điểm Đánh Giá", "Lời Nhận Xét", "Thời Gian Gửi"};
        reviewTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reviewTable = new JTable(reviewTableModel);
        UITheme.styleTable(reviewTable);

        panel.add(new JScrollPane(reviewTable), BorderLayout.CENTER);
        return panel;
    }

    // ================= LOGIC REFRESH DỮ LIỆU =================
    public void refreshAllData() {
        // Nạp lại dữ liệu từ tệp tin JSON
        AppContext.getInstance().getTripRepository().reload();
        AppContext.getInstance().getVehicleRepository().reload();
        AppContext.getInstance().getReviewRepository().reload();

        // 1. Refresh Metrics
        List<Vehicle> vehicles = vehicleService.getVehiclesByOperator(operator.getId());
        List<Trip> trips = tripService.getTripsByOperator(operator.getId());
        double avgRating = reviewService.getAverageRating(operator.getId());

        long atStationCount = vehicleService.countVehiclesAtStation(operator.getId());
        long inTransitCount = vehicleService.countVehiclesInTransit(operator.getId());

        lblTotalVehicles.setText(String.valueOf(vehicles.size()));
        lblAtStationVehicles.setText(String.valueOf(atStationCount));
        lblInTransitVehicles.setText(String.valueOf(inTransitCount));
        lblTotalTrips.setText(String.valueOf(trips.size()));
        lblAverageRating.setText(String.format("%.1f ⭐", avgRating));

        // 2. Refresh Trips Table
        tripTableModel.setRowCount(0);
        for (Trip t : trips) {
            String driverInfo = t.getDriver() != null ? t.getDriver().getFullName() + " (" + t.getDriver().getPhoneNumber() + ")" : "Chưa gán";
            tripTableModel.addRow(new Object[]{
                    t.getId(),
                    t.getDepartureLocation(),
                    t.getDestinationLocation(),
                    DateTimeUtils.formatDateTime(t.getDepartureTime()),
                    t.getEstimatedDurationHours() + " giờ",
                    DateTimeUtils.formatDateTime(t.getEstimatedArrivalTime()),
                    t.getVehicleType().getDisplayName(),
                    t.getLicensePlate(),
                    driverInfo,
                    String.format("%,.0f", t.getBasePrice()),
                    t.getAvailableSeats() + " / " + t.getTotalSeats(),
                    t.getStatus().getDisplayName()
            });
        }

        // 3. Refresh Vehicles Table
        vehicleTableModel.setRowCount(0);
        for (Vehicle v : vehicles) {
            vehicleTableModel.addRow(new Object[]{
                    v.getId(),
                    v.getLicensePlate(),
                    v.getVehicleType().getDisplayName(),
                    v.getTotalSeats(),
                    v.getStatus().getDisplayName(),
                    v.getStationName(),
                    v.getAmenityDescription()
            });
        }

        // 4. Refresh Reviews Table
        reviewTableModel.setRowCount(0);
        for (var r : reviewService.getReviewsByOperator(operator.getId())) {
            reviewTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getTripId() != null ? r.getTripId() : "-",
                    r.getCustomerName(),
                    r.getRating() + " ⭐",
                    r.getComment(),
                    DateTimeUtils.formatDateTime(r.getCreatedAt())
            });
        }
    }

    // ================= ACTIONS =================
    private void openAddTripDialog() {
        List<Vehicle> operatorVehicles = vehicleService.getVehiclesByOperator(operator.getId());
        if (operatorVehicles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhà xe chưa có phương tiện nào! Vui lòng thêm xe vào đội trước khi tạo chuyến.", "Chưa có xe", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Thêm Chuyến Xe Mới Về Quê", true);
        dialog.setSize(520, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);

        int row = 0;

        // Chọn xe
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Chọn xe phục vụ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JComboBox<Vehicle> cbVehicle = new JComboBox<>(operatorVehicles.toArray(new Vehicle[0]));
        form.add(cbVehicle, gbc);
        row++;

        // Điểm xuất phát
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Điểm đón (Xuất phát):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtDeparture = new JTextField("Hà Nội (Bến xe Giáp Bát)");
        form.add(txtDeparture, gbc);
        row++;

        // Điểm đến (Chặng về quê)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Điểm đến (Về quê):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtDestination = new JTextField("Nam Định (Bến xe Nam Định)");
        form.add(txtDestination, gbc);
        row++;

        // Giờ xuất bến
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Giờ xuất bến (dd/MM/yyyy HH:mm):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtTime = new JTextField(DateTimeUtils.formatDateTime(LocalDateTime.now().plusHours(2)));
        form.add(txtTime, gbc);
        row++;

        // Thời gian di chuyển (giờ)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Thời gian di chuyển (giờ):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtDuration = new JTextField("2.0");
        form.add(txtDuration, gbc);
        row++;

        // Giá vé cơ bản
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Giá vé cơ bản (VNĐ):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtPrice = new JTextField("120000");
        form.add(txtPrice, gbc);
        row++;

        // Thông tin tài xế
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Họ tên tài xế:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtDriverName = new JTextField("Nguyễn Văn Hùng");
        form.add(txtDriverName, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("SĐT tài xế:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtDriverPhone = new JTextField("0912345678");
        form.add(txtDriverPhone, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        form.add(new JLabel("Số GPLX & Kinh nghiệm:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField txtDriverLicense = new JTextField("GPLX hạng E - 10 năm KN");
        form.add(txtDriverLicense, gbc);
        row++;

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = UITheme.createPrimaryButton("Lưu Chuyến Xe");
        btnSave.addActionListener(e -> {
            try {
                Vehicle selectedVehicle = (Vehicle) cbVehicle.getSelectedItem();
                if (selectedVehicle == null) return;

                String dep = txtDeparture.getText().trim();
                String dest = txtDestination.getText().trim();
                LocalDateTime depTime = DateTimeUtils.parseDateTime(txtTime.getText().trim());
                if (depTime == null) {
                    JOptionPane.showMessageDialog(dialog, "Định dạng thời gian không đúng! Mẫu chuẩn: dd/MM/yyyy HH:mm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double duration = Double.parseDouble(txtDuration.getText().trim());
                double price = Double.parseDouble(txtPrice.getText().trim());

                Driver driver = new Driver("DRV_" + System.currentTimeMillis() % 10000, 
                        txtDriverName.getText().trim(), txtDriverPhone.getText().trim(), txtDriverLicense.getText().trim(), 5);

                tripService.createTrip(operator.getId(), operator.getCompanyName(), 
                        selectedVehicle.getId(), selectedVehicle.getLicensePlate(), selectedVehicle.getVehicleType(), 
                        driver, dep, dest, depTime, duration, price, selectedVehicle.getTotalSeats());

                JOptionPane.showMessageDialog(dialog, "Thêm chuyến xe thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshAllData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Thao tác thất bại", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void openAssignDriverDialog() {
        int row = tripTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chuyến xe từ bảng để gán tài xế!", "Chưa chọn chuyến", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tripId = (String) tripTableModel.getValueAt(row, 0);
        var tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty()) return;

        Trip trip = tripOpt.get();
        Driver currentDriver = trip.getDriver();

        JDialog dialog = new JDialog(this, "Gán / Sửa Thông Tin Tài Xế Cho Chuyến: " + tripId, true);
        dialog.setSize(420, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JTextField txtName = new JTextField(currentDriver != null ? currentDriver.getFullName() : "");
        JTextField txtPhone = new JTextField(currentDriver != null ? currentDriver.getPhoneNumber() : "");
        JTextField txtLicense = new JTextField(currentDriver != null ? currentDriver.getLicenseNumber() : "");

        form.add(new JLabel("Họ tên tài xế:")); form.add(txtName);
        form.add(new JLabel("Số điện thoại tài xế:")); form.add(txtPhone);
        form.add(new JLabel("Số GPLX / Kinh nghiệm:")); form.add(txtLicense);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = UITheme.createPrimaryButton("Cập nhật tài xế");
        btnSave.addActionListener(e -> {
            Driver newDriver = new Driver("DRV_" + System.currentTimeMillis() % 10000, 
                    txtName.getText().trim(), txtPhone.getText().trim(), txtLicense.getText().trim(), 5);
            tripService.assignDriver(tripId, newDriver);
            JOptionPane.showMessageDialog(dialog, "Cập nhật thông tin tài xế thành công!");
            dialog.dispose();
            refreshAllData();
        });

        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void updateSelectedTripStatus(TripStatus newStatus) {
        int row = tripTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chuyến xe từ bảng!", "Chưa chọn chuyến", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tripId = (String) tripTableModel.getValueAt(row, 0);
        tripService.updateTripStatus(tripId, newStatus);
        JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái chuyến xe thành: " + newStatus.getDisplayName(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
        refreshAllData();
    }

    private void openAddVehicleDialog() {
        JDialog dialog = new JDialog(this, "Thêm Xe Mới Vào Đội Xe", true);
        dialog.setSize(440, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JTextField txtPlate = new JTextField();
        JComboBox<VehicleType> cbType = new JComboBox<>(VehicleType.values());
        JTextField txtSeats = new JTextField("36");
        JTextField txtStation = new JTextField("Bến xe Giáp Bát - Hà Nội");

        form.add(new JLabel("Biển số xe:")); form.add(txtPlate);
        form.add(new JLabel("Loại xe:")); form.add(cbType);
        form.add(new JLabel("Tổng số chỗ/giường:")); form.add(txtSeats);
        form.add(new JLabel("Bến đỗ ban đầu:")); form.add(txtStation);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = UITheme.createPrimaryButton("Thêm xe");
        btnSave.addActionListener(e -> {
            try {
                String plate = txtPlate.getText().trim();
                VehicleType type = (VehicleType) cbType.getSelectedItem();
                int seats = Integer.parseInt(txtSeats.getText().trim());
                String station = txtStation.getText().trim();

                vehicleService.addVehicle(operator.getId(), plate, type, seats, station);
                JOptionPane.showMessageDialog(dialog, "Thêm xe mới thành công!");
                dialog.dispose();
                refreshAllData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void updateSelectedVehicleStatus(VehicleStatus newStatus) {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một xe từ bảng!", "Chưa chọn xe", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String vehicleId = (String) vehicleTableModel.getValueAt(row, 0);
        vehicleService.updateVehicleStatus(vehicleId, newStatus);
        JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái xe thành: " + newStatus.getDisplayName(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
        refreshAllData();
    }

    private void deleteSelectedVehicle() {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một xe cần xóa!", "Chưa chọn xe", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String vehicleId = (String) vehicleTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa xe " + vehicleId + " khỏi đội xe?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            vehicleService.deleteVehicle(vehicleId);
            refreshAllData();
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
        new LoginFrame().setVisible(true);
        dispose();
    }
}
