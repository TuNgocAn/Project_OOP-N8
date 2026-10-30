package vn.busapp.nhaxe.entity;

import jakarta.persistence.*;
import lombok.*;

/** NHA_XE: PK = MaUser (quan hệ 1:1 với USER). */
@Entity @Table(name = "nha_xe")
@Getter @Setter @NoArgsConstructor
public class NhaXe {
    @Id private Long maUser;
    @Column(nullable = false) private String tenNhaXe;
    private String diaChi;
    private String soDienThoai;
    @Column(length = 1000) private String moTa;
}
