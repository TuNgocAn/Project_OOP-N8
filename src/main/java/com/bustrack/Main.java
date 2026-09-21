package com.bustrack;

import com.bustrack.ui.LoginFrame;
import com.bustrack.ui.UITheme;
import com.bustrack.util.AppContext;
import com.bustrack.util.MockDataInitializer;

import javax.swing.*;

/**
 * Điểm khởi chạy chính của Ứng dụng BusTrack (BTL OOP)
 */
public class Main {
    public static void main(String[] args) {
        // 1. Cấu hình giao diện FlatLaf hiện đại
        UITheme.setupLaf();

        // 2. Khởi tạo dữ liệu mẫu nếu ứng dụng chạy lần đầu
        AppContext context = AppContext.getInstance();
        MockDataInitializer.initializeIfEmpty(context);

        // 3. Khởi chạy giao diện Đăng nhập trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
