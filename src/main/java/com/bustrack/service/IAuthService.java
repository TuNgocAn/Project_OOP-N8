package com.bustrack.service;

import com.bustrack.model.user.BusOperator;
import com.bustrack.model.user.Customer;
import com.bustrack.model.user.User;

import java.util.Optional;

/**
 * Interface cho dịch vụ xác thực tài khoản (OOP: Abstraction)
 */
public interface IAuthService {
    Optional<User> login(String username, String password);
    Customer registerCustomer(String username, String password, String fullName, String phone, String email, String hometown);
    BusOperator registerOperator(String username, String password, String fullName, String phone, String companyName, String hotline, String officeAddress);
    boolean changePassword(String userId, String oldPassword, String newPassword);
    boolean updateCustomerProfile(String customerId, String fullName, String phone, String email, String hometown);
}
