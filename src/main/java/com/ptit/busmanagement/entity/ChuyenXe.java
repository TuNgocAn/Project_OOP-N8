package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chuyen_xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuyenXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maChuyen;

    @Column(nullable = false)
    private Long maNhaXe;

    @Column(nullable = false)
    private Long maXe;

    @Column(nullable = false)
    private Long maTaiXe;

    @Column(nullable = false)
    private Long maTuyen;

    @Column(nullable = false)
    private LocalDateTime khoiHanh;

    private LocalDateTime duKienDen;

    @Column(nullable = false)
    private Long giaVe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrangThaiChuyenXe trangThai;

    @Transient
    private String tenNhaXe;

    @Transient
    private String bienSoXe;

    @Transient
    private String loaiXe;

    @Transient
    private Integer soCho;

    @Transient
    private String tenTaiXe;

    @Transient
    private String tenTuyen;

    @Transient
    private String diemDi;

    @Transient
    private String diemDen;

    @Transient
    private Integer soGheTrong;
}
