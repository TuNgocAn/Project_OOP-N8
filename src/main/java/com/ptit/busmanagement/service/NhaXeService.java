package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.NhaXeRequest;
import com.ptit.busmanagement.entity.NhaXe;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.ChuyenXeRepository;
import com.ptit.busmanagement.repository.NhaXeRepository;
import com.ptit.busmanagement.repository.TaiXeRepository;
import com.ptit.busmanagement.repository.UserRepository;
import com.ptit.busmanagement.repository.XeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NhaXeService {
    private final NhaXeRepository repo;
    private final UserRepository userRepo;
    private final XeRepository xeRepo;
    private final TaiXeRepository taiXeRepo;
    private final ChuyenXeRepository chuyenXeRepo;

    public NhaXe create(NhaXeRequest r) {
        if (r.maUser() == null) {
            throw new ConflictException("maUser là bắt buộc khi tạo nhà xe");
        }
        if (!userRepo.existsById(r.maUser())) {
            throw new NotFoundException("Không tìm thấy USER " + r.maUser());
        }
        if (repo.existsById(r.maUser())) {
            throw new ConflictException("USER " + r.maUser() + " đã có nhà xe (quan hệ 1:1)");
        }
        NhaXe n = new NhaXe();
        n.setMaUser(r.maUser());
        apply(n, r);
        return repo.save(n);
    }

    @Transactional(readOnly = true)
    public List<NhaXe> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public NhaXe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy nhà xe ID: " + id));
    }

    public NhaXe update(Long id, NhaXeRequest r) {
        NhaXe n = get(id);
        apply(n, r);
        return repo.save(n);
    }

    public NhaXe updateStatus(Long id, String status) {
        NhaXe n = get(id);
        n.setTrangThai(status);
        return repo.save(n);
    }

    public void delete(Long id) {
        NhaXe n = get(id);
        if (xeRepo.existsByMaNhaXe(id) || taiXeRepo.existsByMaNhaXe(id) || !chuyenXeRepo.findByMaNhaXe(id).isEmpty()) {
            throw new ConflictException("Nhà xe đang còn xe, tài xế hoặc chuyến xe hoạt động, vui lòng xử lý trước khi xóa");
        }
        repo.delete(n);
    }

    private void apply(NhaXe n, NhaXeRequest r) {
        n.setTenNhaXe(r.tenNhaXe());
        n.setDiaChi(r.diaChi());
        n.setSoDienThoai(r.soDienThoai());
        n.setMoTa(r.moTa());
        if (r.trangThai() != null && !r.trangThai().isBlank()) {
            n.setTrangThai(r.trangThai());
        }
    }
}
