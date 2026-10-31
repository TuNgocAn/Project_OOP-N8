package TheoDoiChuyenXe.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ChuyenXeDTO {

    private Integer maChuyen;
    private String diemDi;
    private String diemDen;
    private LocalDate ngayKhoiHanh;
    private LocalTime gioKhoiHanh;
    private LocalTime gioDuKienDen;
    private BigDecimal giaVe;
    private String tenNhaXe;
    private String hoTenTaiXe;
    private String bienSo;
    private String trangThai;

    public ChuyenXeDTO() {
    }

    public ChuyenXeDTO(
            Integer maChuyen,
            String diemDi,
            String diemDen,
            LocalDate ngayKhoiHanh,
            LocalTime gioKhoiHanh,
            LocalTime gioDuKienDen,
            BigDecimal giaVe,
            String tenNhaXe,
            String hoTenTaiXe,
            String bienSo,
            String trangThai) {

        this.maChuyen = maChuyen;
        this.diemDi = diemDi;
        this.diemDen = diemDen;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.gioKhoiHanh = gioKhoiHanh;
        this.gioDuKienDen = gioDuKienDen;
        this.giaVe = giaVe;
        this.tenNhaXe = tenNhaXe;
        this.hoTenTaiXe = hoTenTaiXe;
        this.bienSo = bienSo;
        this.trangThai = trangThai;
    }

    public Integer getMaChuyen() {
        return maChuyen;
    }

    public String getDiemDi() {
        return diemDi;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public LocalDate getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public LocalTime getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public LocalTime getGioDuKienDen() {
        return gioDuKienDen;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public String getTenNhaXe() {
        return tenNhaXe;
    }

    public String getHoTenTaiXe() {
        return hoTenTaiXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    // PK
    private Integer maNhaXe;
    private Integer maXe;
    private Integer maTaiXe;
    private Integer maLichTrinh;

    public Integer getMaNhaXe() { return maNhaXe; }
    public void setMaNhaXe(Integer maNhaXe) { this.maNhaXe = maNhaXe; }

    public Integer getMaXe() { return maXe; }
    public void setMaXe(Integer maXe) { this.maXe = maXe; }

    public Integer getMaTaiXe() { return maTaiXe; }
    public void setMaTaiXe(Integer maTaiXe) { this.maTaiXe = maTaiXe; }

    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
}
