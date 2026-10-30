package vn.busapp.nhaxe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "xe")
@Getter @Setter @NoArgsConstructor
public class Xe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long maXe;
    @Column(nullable = false, unique = true) private String bienSo;
    @Column(nullable = false) private String loaiXe;
    @Column(nullable = false) private Integer soCho;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private XeTrangThai trangThai;
    @Column(nullable = false) private Long maNhaXe;
}
