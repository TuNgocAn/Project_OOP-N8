package TheoDoiChuyenXe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "XE")
public class Xe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaXe")
    private Integer maXe;

    @Column(name = "BienSo")
    private String bienSo;

    @Column(name = "LoaiXe")
    private String loaiXe;

    @Column(name = "SoCho")
    private Integer soCho;

    @Column(name = "TrangThai")
    private String trangThai;

    // Quan hệ nhiều xe thuộc một nhà xe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhaXe")
    private NhaXe nhaXe;

    // Quan hệ nhiều xe thuộc một tài xế
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiXe")
    private TaiXe taiXe;

    public Xe() {
    }

    public Integer getMaXe() {
        return maXe;
    }

    public void setMaXe(Integer maXe) {
        this.maXe = maXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public Integer getSoCho() {
        return soCho;
    }

    public void setSoCho(Integer soCho) {
        this.soCho = soCho;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public NhaXe getNhaXe() {
        return nhaXe;
    }

    public void setNhaXe(NhaXe nhaXe) {
        this.nhaXe = nhaXe;
    }

    public TaiXe getTaiXe() {
        return taiXe;
    }

    public void setTaiXe(TaiXe taiXe) {
        this.taiXe = taiXe;
    }
}
