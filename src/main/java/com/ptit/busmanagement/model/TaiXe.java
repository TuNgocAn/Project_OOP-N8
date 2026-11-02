package com.ptit.busmanagement.model;

import com.ptit.busmanagement.entity.TrangThaiTaiXe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Lớp TaiXe (Mục 4 & 7):
 * Lưu thông tin tài xế được gắn với chuyến xe, giấy phép lái xe, trạng thái làm việc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiXe {
    private Long maTaiXe;
    private Long maNhaXe;
    private String hoTen;
    private String soDienThoai;
    private String soGplx;
    private String hangBang;
    private LocalDate ngaySinh;
    private LocalDate ngayHetHanGplx;
    private TrangThaiTaiXe trangThai;
}
