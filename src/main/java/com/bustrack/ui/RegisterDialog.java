package com.bustrack.ui;

import com.bustrack.model.user.Role;
import com.bustrack.service.IAuthService;
import com.bustrack.util.AppContext;

import javax.swing.*;
import java.awt.*;

/**
 * Hộp thoại Đăng ký tài khoản cho Khách hàng hoặc Nhà xe
 */
public class RegisterDialog extends JDialog {
    private final IAuthService authService = AppContext.getInstance().getAuthService();

    private JComboBox<String> roleCombo;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JTextField txtPhone;
    private JTextField txtEmailOrHotline;
    private JLabel lblEmailOrHotline;
    private JTextField txtHometownOrAddress;
    private JLabel lblHometownOrAddress;
    private JTextField txtCompanyName;
    private JLabel lblCompanyName;

    public RegisterDialog(JFrame parent) {
        super(parent, "Đăng Ký Tài Khoản Mới", true);
        initUI();
    }

    private void initUI() {
        setSize(480, 560);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN BUSTRACK");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        int row = 0;

        // Vai trò
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Bạn là:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        roleCombo = new JComboBox<>(new String[]{"Khách hàng (Người tìm xe về quê)", "Nhà xe (Đơn vị vận tải)"});
        roleCombo.setFont(UITheme.FONT_BOLD);
        roleCombo.addActionListener(e -> onRoleChanged());
        formPanel.add(roleCombo, gbc);
        row++;

        // Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUsername = new JTextField();
        formPanel.add(txtUsername, gbc);
        row++;

        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword, gbc);
        row++;

        // Họ tên
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtFullName = new JTextField();
        formPanel.add(txtFullName, gbc);
        row++;

        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPhone = new JTextField();
        formPanel.add(txtPhone, gbc);
        row++;

        // Tên nhà xe (Chỉ hiện khi là Nhà xe)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        lblCompanyName = new JLabel("Tên nhà xe:");
        formPanel.add(lblCompanyName, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCompanyName = new JTextField();
        formPanel.add(txtCompanyName, gbc);
        row++;

        // Email hoặc Hotline
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        lblEmailOrHotline = new JLabel("Email liên hệ:");
        formPanel.add(lblEmailOrHotline, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtEmailOrHotline = new JTextField();
        formPanel.add(txtEmailOrHotline, gbc);
        row++;

        // Quê quán hoặc Địa chỉ văn phòng
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        lblHometownOrAddress = new JLabel("Quê quán:");
        formPanel.add(lblHometownOrAddress, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtHometownOrAddress = new JTextField();
        formPanel.add(txtHometownOrAddress, gbc);
        row++;

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnCancel = new JButton("Hủy bỏ");
        btnCancel.addActionListener(e -> dispose());

        JButton btnRegister = UITheme.createPrimaryButton("Hoàn tất đăng ký");
        btnRegister.addActionListener(e -> performRegister());

        btnPanel.add(btnCancel);
        btnPanel.add(btnRegister);
        add(btnPanel, BorderLayout.SOUTH);

        onRoleChanged(); // Cập nhật hiển thị ban đầu
    }

    private void onRoleChanged() {
        boolean isOperator = roleCombo.getSelectedIndex() == 1;
        lblCompanyName.setVisible(isOperator);
        txtCompanyName.setVisible(isOperator);

        if (isOperator) {
            lblEmailOrHotline.setText("Hotline nhà xe:");
            lblHometownOrAddress.setText("Địa chỉ bến/văn phòng:");
        } else {
            lblEmailOrHotline.setText("Email cá nhân:");
            lblHometownOrAddress.setText("Quê quán:");
        }
        revalidate();
        repaint();
    }

    private void performRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String fullName = txtFullName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các thông tin bắt buộc!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean isOperator = roleCombo.getSelectedIndex() == 1;
            if (isOperator) {
                String companyName = txtCompanyName.getText().trim();
                String hotline = txtEmailOrHotline.getText().trim();
                String address = txtHometownOrAddress.getText().trim();
                if (companyName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà xe!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                authService.registerOperator(username, password, fullName, phone, companyName, hotline, address);
                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản Nhà xe thành công!\nVui lòng đăng nhập để bắt đầu quản lý.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String email = txtEmailOrHotline.getText().trim();
                String hometown = txtHometownOrAddress.getText().trim();
                authService.registerCustomer(username, password, fullName, phone, email, hometown);
                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản Khách hàng thành công!\nBạn có thể đăng nhập để tìm xe về quê ngay.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Đăng ký thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }
}
