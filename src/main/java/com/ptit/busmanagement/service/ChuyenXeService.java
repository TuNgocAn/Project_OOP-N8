package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.ChuyenXeRequest;
import com.ptit.busmanagement.entity.*;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuyenXeService {
    private final ChuyenXeRepository repo;
    private final NhaXeRepository nhaXeRepo;
    private final XeRepository xeRepo;
    private final TaiXeRepository taiXeRepo;
    private final TuyenXeRepository tuyenXeRepo;
    private final VeXeRepository veXeRepo;

    public ChuyenXe create(ChuyenXeRequest r) {
        checkNhaXe(r.maNhaXe());
        Xe xe = xeRepo.findById(r.maXe())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe ID: " + r.maXe()));
        if (!xe.getMaNhaXe().equals(r.maNhaXe())) {
            throw new ConflictException("Xe " + xe.getBienSo() + " không thuộc sở hữu của nhà xe này");
        }

        TaiXe taiXe = taiXeRepo.findById(r.maTaiXe())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tài xế ID: " + r.maTaiXe()));
        if (!taiXe.getMaNhaXe().equals(r.maNhaXe())) {
            throw new ConflictException("Tài xế " + taiXe.getHoTen() + " không thuộc nhà xe này");
        }

        TuyenXe tuyen = tuyenXeRepo.findById(r.maTuyen())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tuyến đường ID: " + r.maTuyen()));

        LocalDateTime duKienDen = r.duKienDen();
        if (duKienDen == null && tuyen.getThoiGianDuKienPhut() != null) {
            duKienDen = r.khoiHanh().plusMinutes(tuyen.getThoiGianDuKienPhut());
        }

        ChuyenXe cx = ChuyenXe.builder()
                .maNhaXe(r.maNhaXe())
                .maXe(r.maXe())
                .maTaiXe(r.maTaiXe())
                .maTuyen(r.maTuyen())
                .khoiHanh(r.khoiHanh())
                .duKienDen(duKienDen)
                .giaVe(r.giaVe())
                .trangThai(r.trangThai() != null ? r.trangThai() : TrangThaiChuyenXe.SAP_KHOI_HANH)
                .build();

        cx = repo.save(cx);

        // Cập nhật trạng thái tài xế sang ĐANG_LAI nếu chuyến xe đang chạy
        if (cx.getTrangThai() == TrangThaiChuyenXe.DANG_CHAY) {
            taiXe.setTrangThai(TrangThaiTaiXe.DANG_LAI);
            xe.setTrangThai(TrangThaiXe.XE_DI);
            taiXeRepo.save(taiXe);
            xeRepo.save(xe);
        }

        return populate(cx);
    }

    @Transactional(readOnly = true)
    public List<ChuyenXe> findAll(Long maNhaXe, TrangThaiChuyenXe trangThai) {
        List<ChuyenXe> list = maNhaXe != null ? repo.findByMaNhaXe(maNhaXe) : repo.findAll();
        if (trangThai != null) {
            list = list.stream().filter(c -> c.getTrangThai() == trangThai).collect(Collectors.toList());
        }
        list.forEach(this::populate);
        return list;
    }

    @Transactional(readOnly = true)
    public ChuyenXe get(Long id) {
        ChuyenXe cx = repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + id));
        return populate(cx);
    }

    public ChuyenXe update(Long id, ChuyenXeRequest r) {
        ChuyenXe cx = repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + id));
        checkNhaXe(r.maNhaXe());
        cx.setMaNhaXe(r.maNhaXe());
        cx.setMaXe(r.maXe());
        cx.setMaTaiXe(r.maTaiXe());
        cx.setMaTuyen(r.maTuyen());
        cx.setKhoiHanh(r.khoiHanh());
        cx.setDuKienDen(r.duKienDen());
        cx.setGiaVe(r.giaVe());
        if (r.trangThai() != null) {
            cx.setTrangThai(r.trangThai());
        }
        return populate(repo.save(cx));
    }

    public ChuyenXe updateTrangThai(Long id, TrangThaiChuyenXe tt) {
        ChuyenXe cx = repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + id));
        cx.setTrangThai(tt);

        // Đồng bộ trạng thái xe và tài xế
        Xe xe = xeRepo.findById(cx.getMaXe()).orElse(null);
        TaiXe taiXe = taiXeRepo.findById(cx.getMaTaiXe()).orElse(null);

        if (tt == TrangThaiChuyenXe.DANG_CHAY) {
            if (xe != null) xe.setTrangThai(TrangThaiXe.XE_DI);
            if (taiXe != null) taiXe.setTrangThai(TrangThaiTaiXe.DANG_LAI);
        } else if (tt == TrangThaiChuyenXe.HOAN_THANH || tt == TrangThaiChuyenXe.DA_HUY) {
            if (xe != null) xe.setTrangThai(TrangThaiXe.CON_O_BEN);
            if (taiXe != null) taiXe.setTrangThai(TrangThaiTaiXe.RANH);
        }

        if (xe != null) xeRepo.save(xe);
        if (taiXe != null) taiXeRepo.save(taiXe);

        return populate(repo.save(cx));
    }

    public void delete(Long id) {
        ChuyenXe cx = repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + id));
        List<VeXe> ves = veXeRepo.findByMaChuyenAndTrangThai(id, TrangThaiVe.DA_DAT);
        if (!ves.isEmpty()) {
            throw new ConflictException("Chuyến xe đã có vé được khách hàng đặt trước (" + ves.size() + " vé), không thể xóa trực tiếp!");
        }
        repo.delete(cx);
    }

    @Transactional(readOnly = true)
    public List<ChuyenXe> search(String diemDi, String diemDen, LocalDate ngayDi) {
        List<ChuyenXe> all = repo.findAll();
        return all.stream()
                .filter(c -> c.getTrangThai() != TrangThaiChuyenXe.DA_HUY)
                .map(this::populate)
                .filter(c -> {
                    boolean matchDi = (diemDi == null || diemDi.isBlank())
                            || (c.getDiemDi() != null && c.getDiemDi().toLowerCase().contains(diemDi.trim().toLowerCase()));
                    boolean matchDen = (diemDen == null || diemDen.isBlank())
                            || (c.getDiemDen() != null && c.getDiemDen().toLowerCase().contains(diemDen.trim().toLowerCase()));
                    boolean matchNgay = (ngayDi == null)
                            || (c.getKhoiHanh() != null && c.getKhoiHanh().toLocalDate().isEqual(ngayDi));
                    return matchDi && matchDen && matchNgay;
                })
                .collect(Collectors.toList());
    }

    public ChuyenXe populate(ChuyenXe cx) {
        nhaXeRepo.findById(cx.getMaNhaXe()).ifPresent(n -> cx.setTenNhaXe(n.getTenNhaXe()));
        xeRepo.findById(cx.getMaXe()).ifPresent(x -> {
            cx.setBienSoXe(x.getBienSo());
            cx.setLoaiXe(x.getLoaiXe());
            cx.setSoCho(x.getSoCho());
        });
        taiXeRepo.findById(cx.getMaTaiXe()).ifPresent(t -> cx.setTenTaiXe(t.getHoTen()));
        tuyenXeRepo.findById(cx.getMaTuyen()).ifPresent(t -> {
            cx.setTenTuyen(t.getTenTuyen());
            cx.setDiemDi(t.getDiemDi());
            cx.setDiemDen(t.getDiemDen());
        });

        int soCho = cx.getSoCho() != null ? cx.getSoCho() : 40;
        int daDat = veXeRepo.findByMaChuyenAndTrangThai(cx.getMaChuyen(), TrangThaiVe.DA_DAT).size();
        cx.setSoGheTrong(Math.max(0, soCho - daDat));
        return cx;
    }

    private void checkNhaXe(Long maNhaXe) {
        if (!nhaXeRepo.existsById(maNhaXe)) {
            throw new NotFoundException("Không tìm thấy nhà xe ID: " + maNhaXe);
        }
    }
}
