package com.bustrack.ui;

import com.bustrack.model.user.BusOperator;
import com.bustrack.model.user.Customer;
import com.bustrack.model.user.User;
import com.bustrack.service.IAuthService;
import com.bustrack.ui.customer.CustomerMainFrame;
import com.bustrack.ui.operator.OperatorMainFrame;
import com.bustrack.util.AppContext;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * Màn hình đăng nhập hệ thống BusTrack
 */
public class LoginFrame extends JFrame {
    private final IAuthService authService = AppContext.getInstance().getAuthService();

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        super("BusTrack - Đăng Nhập Hệ Thống Theo Dõi Giờ Xe");
        initUI();
    }

    private void initUI() {
        setSize(480, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setBackground(UITheme.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("BUSTRACK VIỆT NAM", SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Theo dõi giờ xe về quê & Quản lý bến bãi", SwingConstants.CENTER);
        lblSub.setFont(UITheme.FONT_REGULAR);
        lblSub.setForeground(new Color(220, 235, 255));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);
        add(headerPanel, BorderLayout.NORTH);

        // Main Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 35, 15, 35));
        centerPanel.setBackground(Color.WHITE);

        // Form fields
        JPanel form = new JPanel(new GridLayout(4, 1, 0, 6));
        form.setOpaque(false);

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(UITheme.FONT_BOLD);
        txtUsername = new JTextField("khachhang");
        txtUsername.setPreferredSize(new Dimension(200, 34));

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(UITheme.FONT_BOLD);
        txtPassword = new JPasswordField("123456");
        txtPassword.setPreferredSize(new Dimension(200, 34));

        form.add(lblUser);
        form.add(txtUsername);
        form.add(lblPass);
        form.add(txtPassword);

        centerPanel.add(form);
        centerPanel.add(Box.createVerticalStrut(15));

        // Login & Register Buttons
        JButton btnLogin = UITheme.createPrimaryButton("ĐĂNG NHẬP");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.addActionListener(e -> performLogin());

        JButton btnRegister = new JButton("Chưa có tài khoản? Đăng ký ngay");
        btnRegister.setFont(UITheme.FONT_REGULAR);
        btnRegister.setForeground(UITheme.PRIMARY);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.addActionListener(e -> openRegisterDialog());

        centerPanel.add(btnLogin);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(btnRegister);
        centerPanel.add(Box.createVerticalStrut(20));

        // Fast Demo Selector Box
        JPanel demoBox = new JPanel();
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setBorder(BorderFactory.createTitledBorder("Đăng Nhập Nhanh (Dành cho Demo / Chấm BTL)"));
        demoBox.setBackground(new Color(248, 250, 252));

        JButton btnDemoCustomer = new JButton("👤 Đăng nhập Khách Hàng (khachhang)");
        btnDemoCustomer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDemoCustomer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btnDemoCustomer.addActionListener(e -> quickLogin("khachhang", "123456"));

        JButton btnDemoOperator = new JButton("🚌 Đăng nhập Nhà Xe Hoàng Long (hoanglong)");
        btnDemoOperator.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDemoOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btnDemoOperator.addActionListener(e -> quickLogin("hoanglong", "123456"));

        JButton btnDemoOperator2 = new JButton("🚌 Đăng nhập Nhà Xe Phương Trang (phuongtrang)");
        btnDemoOperator2.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDemoOperator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btnDemoOperator2.addActionListener(e -> quickLogin("phuongtrang", "123456"));

        demoBox.add(Box.createVerticalStrut(5));
        demoBox.add(btnDemoCustomer);
        demoBox.add(Box.createVerticalStrut(6));
        demoBox.add(btnDemoOperator);
        demoBox.add(Box.createVerticalStrut(6));
        demoBox.add(btnDemoOperator2);
        demoBox.add(Box.createVerticalStrut(5));

        centerPanel.add(demoBox);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            redirectToMain(user);
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không chính xác!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quickLogin(String username, String password) {
        txtUsername.setText(username);
        txtPassword.setText(password);
        performLogin();
    }

    private void redirectToMain(User user) {
        if (user instanceof BusOperator) {
            new OperatorMainFrame((BusOperator) user).setVisible(true);
        } else if (user instanceof Customer) {
            new CustomerMainFrame((Customer) user).setVisible(true);
        }
        dispose();
    }

    private void openRegisterDialog() {
        new RegisterDialog(this).setVisible(true);
    }
}
