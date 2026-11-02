package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "danh_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDanhGia;

    @Column(nullable = false)
    private Long maChuyen;

    private Long maNhaXe;
    private Long maKhachHang;

    @Column(nullable = false, length = 100)
    private String tenKhachHang;

    @Column(nullable = false)
    private Integer soSao;

    @Column(length = 1000)
    private String noiDung;

    private LocalDateTime ngayDanhGia;

    @PrePersist
    public void prePersist() {
        if (ngayDanhGia == null) ngayDanhGia = LocalDateTime.now();
    }
}
