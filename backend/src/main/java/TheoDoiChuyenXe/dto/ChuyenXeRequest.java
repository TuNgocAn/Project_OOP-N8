package TheoDoiChuyenXe.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ChuyenXeRequest {

    @NotNull(message = "Ngày khởi hành không được để trống")
    private LocalDate ngayKhoiHanh;

    @NotNull(message = "Giờ khởi hành không được để trống")
    private LocalTime gioKhoiHanh;

    @NotNull(message = "Giờ dự kiến đến không được để trống")
    private LocalTime gioDuKienDen;

    @NotNull(message = "Giá vé không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá vé phải lớn hơn 0")
    private BigDecimal giaVe;

    @NotNull(message = "Phải chọn nhà xe")
    private Integer maNhaXe;

    @NotNull(message = "Phải chọn xe")
    private Integer maXe;

    @NotNull(message = "Phải chọn tài xế")
    private Integer maTaiXe;

    @NotNull(message = "Phải chọn lịch trình")
    private Integer maLichTrinh;

    // trangThai khi tao moi se mac dinh la "ChuaChay"
    private String trangThai;

    public ChuyenXeRequest() {
    }

    public LocalDate getNgayKhoiHanh() { return ngayKhoiHanh; }
    public void setNgayKhoiHanh(LocalDate ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }

    public LocalTime getGioKhoiHanh() { return gioKhoiHanh; }
    public void setGioKhoiHanh(LocalTime gioKhoiHanh) { this.gioKhoiHanh = gioKhoiHanh; }

    public LocalTime getGioDuKienDen() { return gioDuKienDen; }
    public void setGioDuKienDen(LocalTime gioDuKienDen) { this.gioDuKienDen = gioDuKienDen; }

    public BigDecimal getGiaVe() { return giaVe; }
    public void setGiaVe(BigDecimal giaVe) { this.giaVe = giaVe; }

    public Integer getMaNhaXe() { return maNhaXe; }
    public void setMaNhaXe(Integer maNhaXe) { this.maNhaXe = maNhaXe; }

    public Integer getMaXe() { return maXe; }
    public void setMaXe(Integer maXe) { this.maXe = maXe; }

    public Integer getMaTaiXe() { return maTaiXe; }
    public void setMaTaiXe(Integer maTaiXe) { this.maTaiXe = maTaiXe; }

    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}