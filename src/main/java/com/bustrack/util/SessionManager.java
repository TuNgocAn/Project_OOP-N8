package com.bustrack.util;

import com.bustrack.model.user.BusOperator;
import com.bustrack.model.user.Customer;
import com.bustrack.model.user.Role;
import com.bustrack.model.user.User;

/**
 * Quản lý phiên làm việc hiện tại của người dùng đăng nhập (OOP: Singleton Pattern)
 */
public class SessionManager {
    private static volatile SessionManager instance;
    private User currentUser;

    private SessionManager() {
        // Khởi tạo private để ngăn việc tạo instance từ bên ngoài (Singleton)
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isOperator() {
        return currentUser != null && currentUser.getRole() == Role.OPERATOR;
    }

    public boolean isCustomer() {
        return currentUser != null && currentUser.getRole() == Role.CUSTOMER;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Customer getCurrentCustomer() {
        if (isCustomer() && currentUser instanceof Customer) {
            return (Customer) currentUser;
        }
        return null;
    }

    public BusOperator getCurrentOperator() {
        if (isOperator() && currentUser instanceof BusOperator) {
            return (BusOperator) currentUser;
        }
        return null;
    }
}
