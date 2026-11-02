package com.ptit.busmanagement.model;

import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Lớp ChuyenXe (Mục 4, 5 & 7):
 * Đối tượng trung tâm của hệ thống:
 * - Mã chuyến
 * - Điểm đi, điểm đến (thông qua LichTrinh)
 * - Giờ/ngày khởi hành, giờ/ngày dự kiến đến
 * - Giá vé
 * - Xe thực hiện chuyến
 * - Tài xế phụ trách
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenXe {
    private Long maChuyen;
    private Long maNhaXe;
    private Long maXe;
    private Long maTaiXe;
    private Long maTuyen;
    private LichTrinh lichTrinh;
    private LocalDateTime khoiHanh;
    private LocalDateTime duKienDen;
    private Long giaVe;
    private TrangThaiChuyenXe trangThai;
    private Xe xeThucHien;
    private TaiXe taiXePhuTrach;
    private Integer soGheTrong;
}
