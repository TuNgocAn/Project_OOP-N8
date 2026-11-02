package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.TuyenXeRequest;
import com.ptit.busmanagement.entity.TuyenXe;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.ChuyenXeRepository;
import com.ptit.busmanagement.repository.TuyenXeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TuyenXeService {
    private final TuyenXeRepository repo;
    private final ChuyenXeRepository chuyenXeRepo;

    public TuyenXe create(TuyenXeRequest r) {
        TuyenXe t = TuyenXe.builder()
                .tenTuyen(r.tenTuyen())
                .diemDi(r.diemDi().trim())
                .diemDen(r.diemDen().trim())
                .thoiGianDuKienPhut(r.thoiGianDuKienPhut() != null ? r.thoiGianDuKienPhut() : 120)
                .khoangCachKm(r.khoangCachKm() != null ? r.khoangCachKm() : 100.0)
                .build();
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<TuyenXe> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public TuyenXe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy tuyến xe ID: " + id));
    }

    public TuyenXe update(Long id, TuyenXeRequest r) {
        TuyenXe t = get(id);
        t.setTenTuyen(r.tenTuyen());
        t.setDiemDi(r.diemDi().trim());
        t.setDiemDen(r.diemDen().trim());
        t.setThoiGianDuKienPhut(r.thoiGianDuKienPhut());
        t.setKhoangCachKm(r.khoangCachKm());
        return repo.save(t);
    }

    public void delete(Long id) {
        TuyenXe t = get(id);
        if (!chuyenXeRepo.findByMaTuyen(id).isEmpty()) {
            throw new ConflictException("Tuyến đường đang có các chuyến xe liên kết, không thể xóa");
        }
        repo.delete(t);
    }

    @Transactional(readOnly = true)
    public List<TuyenXe> search(String diemDi, String diemDen) {
        if ((diemDi == null || diemDi.isBlank()) && (diemDen == null || diemDen.isBlank())) {
            return repo.findAll();
        }
        return repo.findByDiemDiContainingIgnoreCaseAndDiemDenContainingIgnoreCase(
                diemDi != null ? diemDi.trim() : "",
                diemDen != null ? diemDen.trim() : ""
        );
    }
}
