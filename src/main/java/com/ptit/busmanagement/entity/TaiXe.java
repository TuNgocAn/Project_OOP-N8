package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tai_xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maTaiXe;

    @Column(nullable = false, length = 100)
    private String hoTen;

    private String soDienThoai;

    @Column(nullable = false, unique = true, length = 30)
    private String soGPLX;

    private String loaiBang;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrangThaiTaiXe trangThai;

    @Column(nullable = false)
    private Long maNhaXe;

    public void capNhatTrangThai(TrangThaiTaiXe tt) {
        if (tt != null) {
            this.trangThai = tt;
        }
    }
}
