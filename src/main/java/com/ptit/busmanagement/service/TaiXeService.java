package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.TaiXeRequest;
import com.ptit.busmanagement.entity.ChuyenXe;
import com.ptit.busmanagement.entity.TaiXe;
import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import com.ptit.busmanagement.entity.TrangThaiTaiXe;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.ChuyenXeRepository;
import com.ptit.busmanagement.repository.NhaXeRepository;
import com.ptit.busmanagement.repository.TaiXeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiXeService {
    private final TaiXeRepository repo;
    private final NhaXeRepository nhaXeRepo;
    private final ChuyenXeRepository chuyenXeRepo;

    public TaiXe create(TaiXeRequest r) {
        checkNhaXe(r.maNhaXe());
        String gplx = r.soGPLX().trim().toUpperCase();
        if (repo.existsBySoGPLX(gplx)) {
            throw new ConflictException("Số GPLX đã tồn tại trong hệ thống: " + gplx);
        }
        TaiXe t = new TaiXe();
        apply(t, r);
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<TaiXe> find(Long maNhaXe, TrangThaiTaiXe tt, String tuKhoa) {
        String kw = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        List<TaiXe> ds = maNhaXe != null ? repo.findByMaNhaXe(maNhaXe) : repo.findAll();
        return ds.stream()
                .filter(t -> tt == null || t.getTrangThai() == tt)
                .filter(t -> kw.isEmpty()
                        || (t.getHoTen() != null && t.getHoTen().toLowerCase().contains(kw))
                        || (t.getSoGPLX() != null && t.getSoGPLX().toLowerCase().contains(kw))
                        || (t.getSoDienThoai() != null && t.getSoDienThoai().contains(kw)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaiXe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy tài xế ID: " + id));
    }

    public TaiXe update(Long id, TaiXeRequest r) {
        TaiXe t = get(id);
        checkNhaXe(r.maNhaXe());
        String gplx = r.soGPLX().trim().toUpperCase();
        if (repo.existsBySoGPLXAndMaTaiXeNot(gplx, id)) {
            throw new ConflictException("Số GPLX đã tồn tại: " + gplx);
        }
        apply(t, r);
        return repo.save(t);
    }

    public TaiXe updateTrangThai(Long id, TrangThaiTaiXe tt) {
        TaiXe t = get(id);
        t.capNhatTrangThai(tt);
        return repo.save(t);
    }

    public void delete(Long id) {
        TaiXe t = get(id);
        // Ràng buộc chuyến xe: Chặn xóa tài xế đang có chuyến
        boolean coChuyen = chuyenXeRepo.existsByMaTaiXeAndTrangThaiIn(id,
                List.of(TrangThaiChuyenXe.SAP_KHOI_HANH, TrangThaiChuyenXe.DANG_CHAY));
        if (coChuyen) {
            throw new ConflictException("Tài xế đang có chuyến xe sắp chạy hoặc đang chạy, không thể xóa");
        }
        repo.delete(t);
    }

    private void checkNhaXe(Long maNhaXe) {
        if (!nhaXeRepo.existsById(maNhaXe)) {
            throw new NotFoundException("Không tìm thấy nhà xe ID: " + maNhaXe);
        }
    }

    private void apply(TaiXe t, TaiXeRequest r) {
        t.setHoTen(r.hoTen().trim());
        t.setSoDienThoai(r.soDienThoai());
        t.setSoGPLX(r.soGPLX().trim().toUpperCase());
        t.setLoaiBang(r.loaiBang() != null ? r.loaiBang().trim().toUpperCase() : "D");
        t.setMaNhaXe(r.maNhaXe());
        if (r.trangThai() != null) {
            t.setTrangThai(r.trangThai());
        } else if (t.getTrangThai() == null) {
            t.setTrangThai(TrangThaiTaiXe.RANH);
        }
    }
}
