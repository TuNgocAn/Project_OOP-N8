package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.XeRequest;
import com.ptit.busmanagement.entity.ChuyenXe;
import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import com.ptit.busmanagement.entity.TrangThaiXe;
import com.ptit.busmanagement.entity.Xe;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.ChuyenXeRepository;
import com.ptit.busmanagement.repository.NhaXeRepository;
import com.ptit.busmanagement.repository.XeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class XeService {
    private final XeRepository repo;
    private final NhaXeRepository nhaXeRepo;
    private final ChuyenXeRepository chuyenXeRepo;

    public Xe create(XeRequest r) {
        checkNhaXe(r.maNhaXe());
        String bienSo = r.bienSo().trim().toUpperCase();
        if (repo.existsByBienSo(bienSo)) {
            throw new ConflictException("Biển số đã tồn tại trong hệ thống: " + bienSo);
        }
        Xe x = new Xe();
        apply(x, r);
        return repo.save(x);
    }

    @Transactional(readOnly = true)
    public List<Xe> find(Long maNhaXe, TrangThaiXe tt, String tuKhoa) {
        String kw = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        List<Xe> ds = maNhaXe != null ? repo.findByMaNhaXe(maNhaXe) : repo.findAll();
        return ds.stream()
                .filter(x -> tt == null || x.getTrangThai() == tt)
                .filter(x -> kw.isEmpty()
                        || (x.getBienSo() != null && x.getBienSo().toLowerCase().contains(kw))
                        || (x.getLoaiXe() != null && x.getLoaiXe().toLowerCase().contains(kw)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Xe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy phương tiện ID: " + id));
    }

    public Xe update(Long id, XeRequest r) {
        Xe x = get(id);
        checkNhaXe(r.maNhaXe());
        String bienSo = r.bienSo().trim().toUpperCase();
        if (repo.existsByBienSoAndMaXeNot(bienSo, id)) {
            throw new ConflictException("Biển số đã tồn tại: " + bienSo);
        }
        apply(x, r);
        return repo.save(x);
    }

    /** Cập nhật riêng trạng thái xe tại bến (Xe đến / Xe đi / Còn ở bến). */
    public Xe updateTrangThai(Long id, TrangThaiXe tt) {
        Xe x = get(id);
        x.capNhatTrangThai(tt);
        return repo.save(x);
    }

    public void delete(Long id) {
        Xe x = get(id);
        // Ràng buộc nghiệp vụ: Không thể xóa xe nếu đang có chuyến xe sắp chạy hoặc đang chạy
        boolean coChuyen = chuyenXeRepo.existsByMaXeAndTrangThaiIn(id,
                List.of(TrangThaiChuyenXe.SAP_KHOI_HANH, TrangThaiChuyenXe.DANG_CHAY));
        if (coChuyen) {
            throw new ConflictException("Xe đang được phân công chạy chuyến, không thể xóa");
        }
        repo.delete(x);
    }

    /** Thống kê số lượng xe theo trạng thái tại bến. maNhaXe = null là toàn bến xe. */
    @Transactional(readOnly = true)
    public Map<String, Object> thongKe(Long maNhaXe) {
        List<Xe> ds = maNhaXe != null ? repo.findByMaNhaXe(maNhaXe) : repo.findAll();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("tongSoXe", ds.size());
        m.put("xeDen", ds.stream().filter(x -> x.getTrangThai() == TrangThaiXe.XE_DEN).count());
        m.put("xeDi", ds.stream().filter(x -> x.getTrangThai() == TrangThaiXe.XE_DI).count());
        m.put("conOBen", ds.stream().filter(Xe::conODenBen).count());
        m.put("baoTri", ds.stream().filter(x -> x.getTrangThai() == TrangThaiXe.BAO_TRI).count());
        return m;
    }

    private void checkNhaXe(Long maNhaXe) {
        if (!nhaXeRepo.existsById(maNhaXe)) {
            throw new NotFoundException("Không tìm thấy nhà xe ID: " + maNhaXe);
        }
    }

    private void apply(Xe x, XeRequest r) {
        x.setBienSo(r.bienSo().trim().toUpperCase());
        x.setLoaiXe(r.loaiXe().trim());
        x.setSoCho(r.soCho());
        x.setMaNhaXe(r.maNhaXe());
        if (r.trangThai() != null) {
            x.setTrangThai(r.trangThai());
        } else if (x.getTrangThai() == null) {
            x.setTrangThai(TrangThaiXe.CON_O_BEN);
        }
    }
}
