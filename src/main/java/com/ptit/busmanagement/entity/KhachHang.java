package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * KHACH_HANG: Quan hệ 1:1 với USER (maUser là PK).
 */
@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {
    @Id
    private Long maUser;

    @Column(nullable = false, length = 100)
    private String hoTen;

    private String diaChi;
    private LocalDate ngaySinh;
    private String cccd;
}
