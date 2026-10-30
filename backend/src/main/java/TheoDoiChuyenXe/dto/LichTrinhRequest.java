package TheoDoiChuyenXe.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class LichTrinhRequest {

    @NotBlank(message = "Điểm đi không được để trống")
    @Size(max = 150, message = "Điểm đi tối đa 150 ký tự")
    private String diemDi;

    @NotBlank(message = "Điểm đến không được để trống")
    @Size(max = 150, message = "Điểm đến tối đa 150 ký tự")
    private String diemDen;

    //phut
    @Positive(message = "Thời gian dự kiến phải lớn hơn 0")
    private Integer thoiGianDuKien;

    //km
    @DecimalMin(value = "0.0", inclusive = false, message = "Khoảng cách phải lớn hơn 0")
    private BigDecimal khoangCach;

    public LichTrinhRequest() {
    }

    public String getDiemDi() { return diemDi; }
    public void setDiemDi(String diemDi) { this.diemDi = diemDi; }

    public String getDiemDen() { return diemDen; }
    public void setDiemDen(String diemDen) { this.diemDen = diemDen; }

    public Integer getThoiGianDuKien() { return thoiGianDuKien; }
    public void setThoiGianDuKien(Integer thoiGianDuKien) { this.thoiGianDuKien = thoiGianDuKien; }

    public BigDecimal getKhoangCach() { return khoangCach; }
    public void setKhoangCach(BigDecimal khoangCach) { this.khoangCach = khoangCach; }
}