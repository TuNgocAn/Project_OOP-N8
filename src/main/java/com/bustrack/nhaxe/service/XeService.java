package vn.busapp.nhaxe.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.busapp.nhaxe.dto.XeRequest;
import vn.busapp.nhaxe.entity.*;
import vn.busapp.nhaxe.exception.*;
import vn.busapp.nhaxe.repository.*;

@Service @RequiredArgsConstructor @Transactional
public class XeService {
    private final XeRepository repo;
    private final NhaXeRepository nhaXeRepo;

    public Xe create(XeRequest r) {
        checkNhaXe(r.maNhaXe());
        if (repo.existsByBienSo(r.bienSo())) throw new ConflictException("Biển số đã tồn tại: " + r.bienSo());
        Xe x = new Xe();
        apply(x, r);
        return repo.save(x);
    }

    @Transactional(readOnly = true)
    public List<Xe> find(Long maNhaXe, XeTrangThai tt) {
        if (maNhaXe != null && tt != null) return repo.findByMaNhaXeAndTrangThai(maNhaXe, tt);
        if (maNhaXe != null) return repo.findByMaNhaXe(maNhaXe);
        if (tt != null) return repo.findByTrangThai(tt);
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Xe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy xe " + id));
    }

    public Xe update(Long id, XeRequest r) {
        Xe x = get(id);
        checkNhaXe(r.maNhaXe());
        if (repo.existsByBienSoAndMaXeNot(r.bienSo(), id)) throw new ConflictException("Biển số đã tồn tại: " + r.bienSo());
        apply(x, r);
        return repo.save(x);
    }

    /** Cập nhật riêng trạng thái xe. */
    public Xe updateTrangThai(Long id, XeTrangThai tt) {
        Xe x = get(id);
        x.setTrangThai(tt);
        return repo.save(x);
    }

    public void delete(Long id) { repo.delete(get(id)); }

    private void checkNhaXe(Long maNhaXe) {
        if (!nhaXeRepo.existsById(maNhaXe)) throw new NotFoundException("Không tìm thấy nhà xe " + maNhaXe);
    }

    private void apply(Xe x, XeRequest r) {
        x.setBienSo(r.bienSo());
        x.setLoaiXe(r.loaiXe());
        x.setSoCho(r.soCho());
        x.setMaNhaXe(r.maNhaXe());
        if (r.trangThai() != null) x.setTrangThai(r.trangThai());
        else if (x.getTrangThai() == null) x.setTrangThai(XeTrangThai.SAN_SANG);
    }
}
