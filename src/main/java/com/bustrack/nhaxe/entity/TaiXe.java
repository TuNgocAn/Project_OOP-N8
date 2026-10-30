package vn.busapp.nhaxe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "tai_xe")
@Getter @Setter @NoArgsConstructor
public class TaiXe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long maTaiXe;
    @Column(nullable = false) private String hoTen;
    private String soDienThoai;
    @Column(nullable = false, unique = true) private String soGPLX;
    private String loaiBang;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TaiXeTrangThai trangThai;
    @Column(nullable = false) private Long maNhaXe;
}
