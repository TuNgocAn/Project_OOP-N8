package com.ptit.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Lớp KhachHang (Mục 4 & 7):
 * Kế thừa User, quản lý thông tin và nhu cầu tra cứu của khách hàng.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang extends User {
    private String hoTen;
    private String diaChi;
    private LocalDate ngaySinh;
    private String cccd;
}
