package com.bustrack.factory;

import com.bustrack.model.user.*;

/**
 * Factory khởi tạo các loại tài khoản người dùng (OOP: Factory Method Pattern)
 */
public class UserFactory {

    public static Customer createCustomer(String id, String username, String password, 
                                          String fullName, String phoneNumber, String email, String hometown) {
        return new Customer(id, username, password, fullName, phoneNumber, email, hometown);
    }

    public static BusOperator createOperator(String id, String username, String password, 
                                             String fullName, String phoneNumber, String companyName, 
                                             String hotline, String officeAddress) {
        return new BusOperator(id, username, password, fullName, phoneNumber, companyName, hotline, officeAddress);
    }
}
