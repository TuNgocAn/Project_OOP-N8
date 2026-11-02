package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.response.ApiResponse;
import com.ptit.busmanagement.entity.KhachHang;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * API Quản lý thông tin cá nhân khách hàng (Mục 2.2 & 6).
 */
@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {
    private final KhachHangRepository khachHangRepo;

    @GetMapping
    public List<KhachHang> danhSach() {
        return khachHangRepo.findAll();
    }

    @GetMapping("/{maUser}")
    public KhachHang chiTiet(@PathVariable Long maUser) {
        return khachHangRepo.findById(maUser)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin khách hàng ID: " + maUser));
    }

    @PutMapping("/{maUser}")
    public ApiResponse<KhachHang> capNhat(@PathVariable Long maUser, @RequestBody KhachHang r) {
        KhachHang kh = khachHangRepo.findById(maUser)
                .orElseGet(() -> KhachHang.builder().maUser(maUser).build());
        if (r.getHoTen() != null && !r.getHoTen().isBlank()) kh.setHoTen(r.getHoTen().trim());
        if (r.getDiaChi() != null) kh.setDiaChi(r.getDiaChi());
        if (r.getNgaySinh() != null) kh.setNgaySinh(r.getNgaySinh());
        if (r.getCccd() != null) kh.setCccd(r.getCccd());
        KhachHang saved = khachHangRepo.save(kh);
        return ApiResponse.ok("Cập nhật thông tin khách hàng thành công", saved);
    }
}
