package TheoDoiChuyenXe.controller;

import TheoDoiChuyenXe.dto.ChuyenXeDTO;
import TheoDoiChuyenXe.entity.ChuyenXe;
import TheoDoiChuyenXe.repository.ChuyenXeRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chuyen-xe")
public class ChuyenXeController {

    private final ChuyenXeRepository chuyenXeRepository;

    public ChuyenXeController(
            ChuyenXeRepository chuyenXeRepository) {
        this.chuyenXeRepository = chuyenXeRepository;
    }

    @GetMapping
    public List<ChuyenXeDTO> getAllChuyenXe() {

        List<ChuyenXe> danhSach =
                chuyenXeRepository.findAll();

        return danhSach.stream()
                .map(cx -> new ChuyenXeDTO(
                        cx.getMaChuyen(),
                        cx.getLichTrinh().getDiemDi(),
                        cx.getLichTrinh().getDiemDen(),
                        cx.getNgayKhoiHanh(),
                        cx.getGioKhoiHanh(),
                        cx.getGioDuKienDen(),
                        cx.getGiaVe(),
                        cx.getNhaXe().getTenNhaXe(),
                        cx.getTaiXe().getHoTen(),
                        cx.getXe().getBienSo(),
                        cx.getTrangThai()
                ))
                .toList();
    }
}