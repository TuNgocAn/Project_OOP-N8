package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * NHA_XE: Quan hệ 1:1 với USER (maUser là PK).
 */
@Entity
@Table(name = "nha_xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhaXe {
    @Id
    private Long maUser;

    @Column(nullable = false, length = 100)
    private String tenNhaXe;

    private String diaChi;
    private String soDienThoai;

    @Column(length = 1000)
    private String moTa;

    /** HOAT_DONG, CHO_DUYET, KHOA */
    @Column(length = 20)
    private String trangThai;

    @PrePersist
    public void prePersist() {
        if (trangThai == null) trangThai = "HOAT_DONG";
    }
}
