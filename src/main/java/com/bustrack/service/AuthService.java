package com.bustrack.service;

import com.bustrack.factory.UserFactory;
import com.bustrack.model.user.BusOperator;
import com.bustrack.model.user.Customer;
import com.bustrack.model.user.User;
import com.bustrack.repository.UserRepository;
import com.bustrack.util.SessionManager;
import com.bustrack.util.ValidationUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * Lớp cài đặt dịch vụ xác thực tài khoản
 */
public class AuthService implements IAuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(String username, String password) {
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(password)) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password.trim().equals(user.getPassword())) {
                SessionManager.getInstance().login(user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Customer registerCustomer(String username, String password, String fullName, 
                                     String phone, String email, String hometown) {
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(password)) {
            throw new IllegalArgumentException("Tên đăng nhập và mật khẩu không được để trống!");
        }
        if (userRepository.existsByUsername(username.trim())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại trong hệ thống!");
        }

        String id = "CUS_" + UUID.randomUUID().toString().substring(0, 8);
        Customer customer = UserFactory.createCustomer(id, username.trim(), password.trim(), 
                fullName.trim(), phone != null ? phone.trim() : "", 
                email != null ? email.trim() : "", hometown != null ? hometown.trim() : "");

        userRepository.save(customer);
        return customer;
    }

    @Override
    public BusOperator registerOperator(String username, String password, String fullName, 
                                        String phone, String companyName, String hotline, String officeAddress) {
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(password)) {
            throw new IllegalArgumentException("Tên đăng nhập và mật khẩu không được để trống!");
        }
        if (userRepository.existsByUsername(username.trim())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại trong hệ thống!");
        }

        String id = "OP_" + UUID.randomUUID().toString().substring(0, 8);
        BusOperator operator = UserFactory.createOperator(id, username.trim(), password.trim(), 
                fullName.trim(), phone != null ? phone.trim() : "", 
                companyName.trim(), hotline != null ? hotline.trim() : phone, 
                officeAddress != null ? officeAddress.trim() : "");

        userRepository.save(operator);
        return operator;
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                return userRepository.update(user);
            }
        }
        return false;
    }

    @Override
    public boolean updateCustomerProfile(String customerId, String fullName, String phone, String email, String hometown) {
        Optional<User> opt = userRepository.findById(customerId);
        if (opt.isPresent() && opt.get() instanceof Customer) {
            Customer customer = (Customer) opt.get();
            customer.setFullName(fullName);
            customer.setPhoneNumber(phone);
            customer.setEmail(email);
            customer.setHometown(hometown);
            return userRepository.update(customer);
        }
        return false;
    }
}
