package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Xe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maXe;

    @Column(nullable = false, unique = true, length = 20)
    private String bienSo;

    @Column(nullable = false, length = 50)
    private String loaiXe;

    @Column(nullable = false)
    private Integer soCho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrangThaiXe trangThai;

    @Column(nullable = false)
    private Long maNhaXe;

    public boolean conODenBen() {
        return trangThai == TrangThaiXe.CON_O_BEN;
    }

    public void capNhatTrangThai(TrangThaiXe tt) {
        if (tt != null) {
            this.trangThai = tt;
        }
    }
}
