package vn.busapp.nhaxe.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.busapp.nhaxe.dto.TaiXeRequest;
import vn.busapp.nhaxe.entity.*;
import vn.busapp.nhaxe.exception.*;
import vn.busapp.nhaxe.repository.*;

@Service @RequiredArgsConstructor @Transactional
public class TaiXeService {
    private final TaiXeRepository repo;
    private final NhaXeRepository nhaXeRepo;

    public TaiXe create(TaiXeRequest r) {
        checkNhaXe(r.maNhaXe());
        if (repo.existsBySoGPLX(r.soGPLX())) throw new ConflictException("Số GPLX đã tồn tại: " + r.soGPLX());
        TaiXe t = new TaiXe();
        apply(t, r);
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<TaiXe> find(Long maNhaXe) {
        return maNhaXe != null ? repo.findByMaNhaXe(maNhaXe) : repo.findAll();
    }

    @Transactional(readOnly = true)
    public TaiXe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy tài xế " + id));
    }

    public TaiXe update(Long id, TaiXeRequest r) {
        TaiXe t = get(id);
        checkNhaXe(r.maNhaXe());
        if (repo.existsBySoGPLXAndMaTaiXeNot(r.soGPLX(), id)) throw new ConflictException("Số GPLX đã tồn tại: " + r.soGPLX());
        apply(t, r);
        return repo.save(t);
    }

    public void delete(Long id) { repo.delete(get(id)); }

    private void checkNhaXe(Long maNhaXe) {
        if (!nhaXeRepo.existsById(maNhaXe)) throw new NotFoundException("Không tìm thấy nhà xe " + maNhaXe);
    }

    private void apply(TaiXe t, TaiXeRequest r) {
        t.setHoTen(r.hoTen());
        t.setSoDienThoai(r.soDienThoai());
        t.setSoGPLX(r.soGPLX());
        t.setLoaiBang(r.loaiBang());
        t.setMaNhaXe(r.maNhaXe());
        if (r.trangThai() != null) t.setTrangThai(r.trangThai());
        else if (t.getTrangThai() == null) t.setTrangThai(TaiXeTrangThai.RANH);
    }
}
