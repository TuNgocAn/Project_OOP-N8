package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.DanhGiaRequest;
import com.ptit.busmanagement.entity.ChuyenXe;
import com.ptit.busmanagement.entity.DanhGia;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.ChuyenXeRepository;
import com.ptit.busmanagement.repository.DanhGiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DanhGiaService {
    private final DanhGiaRepository repo;
    private final ChuyenXeRepository chuyenXeRepo;

    public DanhGia danhGia(DanhGiaRequest r) {
        ChuyenXe cx = chuyenXeRepo.findById(r.maChuyen())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + r.maChuyen()));

        DanhGia dg = DanhGia.builder()
                .maChuyen(r.maChuyen())
                .maNhaXe(cx.getMaNhaXe())
                .maKhachHang(r.maKhachHang())
                .tenKhachHang(r.tenKhachHang().trim())
                .soSao(r.soSao())
                .noiDung(r.noiDung())
                .ngayDanhGia(LocalDateTime.now())
                .build();
        return repo.save(dg);
    }

    @Transactional(readOnly = true)
    public List<DanhGia> findByChuyenXe(Long maChuyen) {
        return repo.findByMaChuyen(maChuyen);
    }

    @Transactional(readOnly = true)
    public List<DanhGia> findByNhaXe(Long maNhaXe) {
        return repo.findByMaNhaXe(maNhaXe);
    }

    @Transactional(readOnly = true)
    public List<DanhGia> findAll() {
        return repo.findAll();
    }
}
