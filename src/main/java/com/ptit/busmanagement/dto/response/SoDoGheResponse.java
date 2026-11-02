package com.ptit.busmanagement.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoDoGheResponse {
    private Long maChuyen;
    private Long maXe;
    private String bienSo;
    private String loaiXe;
    private Integer soCho;
    private Long giaVe;
    private List<String> danhSachGheDaDat;
}
