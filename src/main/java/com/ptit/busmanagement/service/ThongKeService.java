package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.response.ThongKeBenXeResponse;
import com.ptit.busmanagement.entity.TrangThaiVe;
import com.ptit.busmanagement.entity.TrangThaiXe;
import com.ptit.busmanagement.entity.VeXe;
import com.ptit.busmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThongKeService {
    private final XeRepository xeRepo;
    private final NhaXeRepository nhaXeRepo;
    private final ChuyenXeRepository chuyenXeRepo;
    private final VeXeRepository veXeRepo;

    public ThongKeBenXeResponse thongKeBenXe() {
        long tongXe = xeRepo.count();
        long xeDen = xeRepo.countByTrangThai(TrangThaiXe.XE_DEN);
        long xeDi = xeRepo.countByTrangThai(TrangThaiXe.XE_DI);
        long conOBen = xeRepo.countByTrangThai(TrangThaiXe.CON_O_BEN);
        long baoTri = xeRepo.countByTrangThai(TrangThaiXe.BAO_TRI);

        long tongNhaXe = nhaXeRepo.count();
        long tongChuyen = chuyenXeRepo.count();
        long tongVe = veXeRepo.countByTrangThai(TrangThaiVe.DA_DAT);

        long doanhThu = veXeRepo.findAll().stream()
                .filter(v -> v.getTrangThai() == TrangThaiVe.DA_DAT)
                .mapToLong(VeXe::getGiaVe)
                .sum();

        return ThongKeBenXeResponse.builder()
                .tongSoXe(tongXe)
                .xeDen(xeDen)
                .xeDi(xeDi)
                .conOBen(conOBen)
                .baoTri(baoTri)
                .tongNhaXe(tongNhaXe)
                .tongChuyen(tongChuyen)
                .tongVeDaBan(tongVe)
                .tongDoanhThu(doanhThu)
                .build();
    }
}
