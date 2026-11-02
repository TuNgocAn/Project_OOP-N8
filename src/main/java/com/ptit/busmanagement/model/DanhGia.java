package com.ptit.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Lớp DanhGia (Mục 4 & 7):
 * Lưu đánh giá của khách hàng đối với chuyến xe sau khi hoàn thành.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {
    private Long maDanhGia;
    private Long maChuyen;
    private Long maNhaXe;
    private Long maKhachHang;
    private String tenKhachHang;
    private Integer soSao;
    private String noiDung;
    private LocalDateTime ngayDanhGia;
}
