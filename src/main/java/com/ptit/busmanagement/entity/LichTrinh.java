package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Thực thể Lịch Trình (LichTrinh) theo đề xuất thiết kế OOP (Mục 4 & 7):
 * Quản lý điểm đi, điểm đến, thời gian di chuyển dự kiến, khoảng cách km, lộ trình dừng đỗ.
 */
@Entity
@Table(name = "lich_trinh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichTrinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLichTrinh;

    @Column(nullable = false, length = 100)
    private String diemDi;

    @Column(nullable = false, length = 100)
    private String diemDen;

    @Column(nullable = false)
    private Integer thoiGianDuKienPhut;

    private Double khoangCachKm;

    @Column(length = 1000)
    private String loTrinhChiTiet;

    private Integer thoiGianDungNghiPhut;
}
