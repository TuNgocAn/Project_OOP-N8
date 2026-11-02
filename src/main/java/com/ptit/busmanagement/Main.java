package com.ptit.busmanagement;

import org.springframework.boot.SpringApplication;

/**
 * Điểm khởi chạy chính của ứng dụng Quản lý Bến xe (Main.java theo cấu trúc Mục 7).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   ỨNG DỤNG THEO DÕI VÀ TRA CỨU CHUYẾN XE - QUẢN LÝ BẾN XE       ");
        System.out.println("   Môi trường: Java JDK 21 | Spring Boot 4.0.8 | Cổng: 8080       ");
        System.out.println("   Truy cập Web UI: http://localhost:8080/                        ");
        System.out.println("==================================================================");
        SpringApplication.run(BusManagementApplication.class, args);
    }
}
