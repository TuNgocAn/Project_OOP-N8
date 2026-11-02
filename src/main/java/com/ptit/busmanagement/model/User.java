package com.ptit.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Lớp cơ sở User (Mục 4 & 7):
 * Chứa thông tin tài khoản dùng chung (KhachHang, NhaXe kế thừa).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long maUser;
    private String username;
    private String password;
    private String email;
    private String soDienThoai;
    private String vaiTro;
    private String trangThai;
    private LocalDateTime ngayTao;
}
