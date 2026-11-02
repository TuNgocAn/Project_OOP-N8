package com.ptit.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp LichTrinh (Mục 4 & 7):
 * Tách riêng thông tin thời gian/lịch trình (điểm đi, điểm đến, thời gian di chuyển dự kiến, lộ trình).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichTrinh {
    private Long maLichTrinh;
    private String diemDi;
    private String diemDen;
    private Integer thoiGianDuKienPhut;
    private Double khoangCachKm;
    private String loTrinhChiTiet;
    private Integer thoiGianDungNghiPhut;
}
