package TheoDoiChuyenXe.entity;

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

    @Column(name = "DiemDi")
    private String diemDi;

    @Column(name = "DiemDen")
    private String diemDen;

    @Column(name = "ThoiGianDuKien")
    private Integer thoiGianDuKien;

    @Column(name = "KhoangCach")
    private Double khoangCach;

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

    public Double getKhoangCach() {
        return khoangCach;
    }

    public void setKhoangCach(Double khoangCach) {
        this.khoangCach = khoangCach;
    }
}