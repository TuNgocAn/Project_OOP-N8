package vn.busapp.nhaxe.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.busapp.nhaxe.dto.NhaXeRequest;
import vn.busapp.nhaxe.entity.NhaXe;
import vn.busapp.nhaxe.exception.*;
import vn.busapp.nhaxe.repository.*;

@Service @RequiredArgsConstructor @Transactional
public class NhaXeService {
    private final NhaXeRepository repo;
    private final UserRepository userRepo;
    private final XeRepository xeRepo;
    private final TaiXeRepository taiXeRepo;

    public NhaXe create(NhaXeRequest r) {
        if (r.maUser() == null) throw new ConflictException("maUser là bắt buộc khi tạo nhà xe");
        if (!userRepo.existsById(r.maUser())) throw new NotFoundException("Không tìm thấy USER " + r.maUser());
        if (repo.existsById(r.maUser())) throw new ConflictException("USER " + r.maUser() + " đã có nhà xe (quan hệ 1:1)");
        NhaXe n = new NhaXe();
        n.setMaUser(r.maUser());
        apply(n, r);
        return repo.save(n);
    }

    @Transactional(readOnly = true)
    public List<NhaXe> findAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public NhaXe get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy nhà xe " + id));
    }

    public NhaXe update(Long id, NhaXeRequest r) {
        NhaXe n = get(id);
        apply(n, r);
        return repo.save(n);
    }

    public void delete(Long id) {
        NhaXe n = get(id);
        if (xeRepo.existsByMaNhaXe(id) || taiXeRepo.existsByMaNhaXe(id))
            throw new ConflictException("Nhà xe còn xe/tài xế, hãy xóa trước");
        repo.delete(n);
    }

    private void apply(NhaXe n, NhaXeRequest r) {
        n.setTenNhaXe(r.tenNhaXe());
        n.setDiaChi(r.diaChi());
        n.setSoDienThoai(r.soDienThoai());
        n.setMoTa(r.moTa());
    }
}
