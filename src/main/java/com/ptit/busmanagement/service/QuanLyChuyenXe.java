package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.ChuyenXeRequest;
import com.ptit.busmanagement.entity.ChuyenXe;
import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Lớp QuanLyChuyenXe theo thiết kế OOP (Mục 4 & 7):
 * Xử lý tạo, cập nhật, xóa, phân công tài xế, xe và quản lý trạng thái chuyến xe.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class QuanLyChuyenXe {
    private final ChuyenXeService chuyenXeService;

    public ChuyenXe taoChuyenXe(ChuyenXeRequest request) {
        return chuyenXeService.create(request);
    }

    public ChuyenXe capNhatChuyenXe(Long id, ChuyenXeRequest request) {
        return chuyenXeService.update(id, request);
    }

    public void xoaChuyenXe(Long id) {
        chuyenXeService.delete(id);
    }

    public ChuyenXe capNhatTrangThaiChuyen(Long id, TrangThaiChuyenXe trangThai) {
        return chuyenXeService.updateTrangThai(id, trangThai);
    }

    @Transactional(readOnly = true)
    public ChuyenXe layChiTietChuyenXe(Long id) {
        return chuyenXeService.get(id);
    }

    @Transactional(readOnly = true)
    public List<ChuyenXe> danhSachChuyenXe(Long maNhaXe, TrangThaiChuyenXe trangThai) {
        return chuyenXeService.findAll(maNhaXe, trangThai);
    }

    @Transactional(readOnly = true)
    public List<ChuyenXe> timKiemChuyenXe(String diemDi, String diemDen, LocalDate ngayDi) {
        return chuyenXeService.search(diemDi, diemDen, ngayDi);
    }
}
