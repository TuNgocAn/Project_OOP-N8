package com.ptit.busmanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongKeBenXeResponse {
    private long tongSoXe;
    private long xeDen;
    private long xeDi;
    private long conOBen;
    private long baoTri;
    private long tongNhaXe;
    private long tongChuyen;
    private long tongVeDaBan;
    private long tongDoanhThu;
}
