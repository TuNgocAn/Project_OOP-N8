package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maUser;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String soDienThoai;

    /** ADMIN, NHA_XE, KHACH_HANG */
    @Column(nullable = false, length = 20)
    private String vaiTro;

    /** HOAT_DONG, KHOA */
    @Column(nullable = false, length = 20)
    private String trangThai;

    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (trangThai == null) trangThai = "HOAT_DONG";
        if (vaiTro == null) vaiTro = "KHACH_HANG";
    }
}
