
package TheoDoiChuyenXe.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LICH_TRINH")
public class LichTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichTrinh")
    private Integer maLichTrinh;

    @Column(name = "DiemDi", nullable = false)
    private String diemDi;

    @Column(name = "DiemDen", nullable = false)
    private String diemDen;

    @Column(name = "ThoiGianDuKien")
    private Integer thoiGianDuKien;

    @Column(name = "KhoangCach", precision = 10, scale = 2)
    private BigDecimal khoangCach;

    public LichTrinh() {
    }

    public Integer getMaLichTrinh() {
        return maLichTrinh;
    }

    public void setMaLichTrinh(Integer maLichTrinh) {
        this.maLichTrinh = maLichTrinh;
    }

    public String getDiemDi() {
        return diemDi;
    }

    public void setDiemDi(String diemDi) {
        this.diemDi = diemDi;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    public Integer getThoiGianDuKien() {
        return thoiGianDuKien;
    }

    public void setThoiGianDuKien(Integer thoiGianDuKien) {
        this.thoiGianDuKien = thoiGianDuKien;
    }

    public BigDecimal getKhoangCach() {
        return khoangCach;
    }

    public void setKhoangCach(BigDecimal khoangCach) {
        this.khoangCach = khoangCach;
    }
}
