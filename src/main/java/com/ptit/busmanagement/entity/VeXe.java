package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ve_xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maVe;

    @Column(nullable = false, unique = true, length = 30)
    private String maVeCode;

    @Column(nullable = false)
    private Long maChuyen;

    private Long maKhachHang;

    @Column(nullable = false, length = 100)
    private String tenKhachHang;

    @Column(nullable = false, length = 20)
    private String soDienThoai;

    private String email;

    @Column(nullable = false, length = 20)
    private String soGhe;

    @Column(nullable = false)
    private Long giaVe;

    private LocalDateTime ngayDat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrangThaiVe trangThai;

    @Transient
    private ChuyenXe chuyenXe;

    @PrePersist
    public void prePersist() {
        if (ngayDat == null) ngayDat = LocalDateTime.now();
        if (trangThai == null) trangThai = TrangThaiVe.DA_DAT;
    }
}
