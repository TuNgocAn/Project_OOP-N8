package com.ptit.busmanagement.service;

import com.ptit.busmanagement.entity.ChuyenXe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lớp TimKiemChuyenXe theo thiết kế OOP (Mục 4 & 7):
 * Xử lý tiêu chí tìm kiếm và lọc chuyến xe theo điểm đi, điểm đến, ngày/giờ đi, khoảng giá và nhà xe.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimKiemChuyenXe {
    private final ChuyenXeService chuyenXeService;

    public List<ChuyenXe> timKiem(String diemDi, String diemDen, LocalDate ngayDi) {
        return chuyenXeService.search(diemDi, diemDen, ngayDi);
    }

    public List<ChuyenXe> locTheoKhoangGia(List<ChuyenXe> danhSach, Long giaMin, Long giaMax) {
        if (danhSach == null) return List.of();
        return danhSach.stream()
                .filter(cx -> (giaMin == null || cx.getGiaVe() >= giaMin) &&
                              (giaMax == null || cx.getGiaVe() <= giaMax))
                .collect(Collectors.toList());
    }

    public List<ChuyenXe> locTheoNhaXe(List<ChuyenXe> danhSach, Long maNhaXe) {
        if (danhSach == null || maNhaXe == null) return danhSach;
        return danhSach.stream()
                .filter(cx -> cx.getMaNhaXe().equals(maNhaXe))
                .collect(Collectors.toList());
    }

    public List<ChuyenXe> sapXepTheoGioKhoiHanh(List<ChuyenXe> danhSach, boolean tangDan) {
        if (danhSach == null) return List.of();
        Comparator<ChuyenXe> comp = Comparator.comparing(ChuyenXe::getKhoiHanh);
        if (!tangDan) comp = comp.reversed();
        return danhSach.stream().sorted(comp).collect(Collectors.toList());
    }

    public List<ChuyenXe> sapXepTheoGiaVe(List<ChuyenXe> danhSach, boolean tangDan) {
        if (danhSach == null) return List.of();
        Comparator<ChuyenXe> comp = Comparator.comparing(ChuyenXe::getGiaVe);
        if (!tangDan) comp = comp.reversed();
        return danhSach.stream().sorted(comp).collect(Collectors.toList());
    }
}
