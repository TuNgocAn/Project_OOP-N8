package com.ptit.busmanagement.model;

import com.ptit.busmanagement.entity.TrangThaiXe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp Xe (Mục 4 & 7):
 * Lưu thông tin phương tiện, biển số, loại xe, số chỗ và trạng thái (XE_DEN, XE_DI, CON_O_BEN, BAO_TRI).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Xe {
    private Long maXe;
    private String bienSo;
    private String loaiXe;
    private Integer soCho;
    private TrangThaiXe trangThai;
    private Long maNhaXe;
    private String hangXe;
    private Integer namSanXuat;
}
