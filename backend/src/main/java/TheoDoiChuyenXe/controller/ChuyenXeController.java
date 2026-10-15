package TheoDoiChuyenXe.controller;

import TheoDoiChuyenXe.dto.ChuyenXeDTO;
import TheoDoiChuyenXe.entity.ChuyenXe;
import TheoDoiChuyenXe.repository.ChuyenXeRepository;

import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public List<ChuyenXeDTO> getAllChuyenXe() {

        List<ChuyenXe> danhSach = chuyenXeRepository.findAll();

        return danhSach.stream()
                .map(cx -> new ChuyenXeDTO(
                        cx.getMaChuyen(),

                        cx.getLichTrinh() != null
                                ? cx.getLichTrinh().getDiemDi()
                                : null,

                        cx.getLichTrinh() != null
                                ? cx.getLichTrinh().getDiemDen()
                                : null,

                        cx.getNgayKhoiHanh(),
                        cx.getGioKhoiHanh(),
                        cx.getGioDuKienDen(),
                        cx.getGiaVe(),

                        cx.getNhaXe() != null
                                ? cx.getNhaXe().getTenNhaXe()
                                : null,

                        cx.getTaiXe() != null
                                ? cx.getTaiXe().getHoTen()
                                : null,

                        cx.getXe() != null
                                ? cx.getXe().getBienSo()
                                : null,

                        cx.getTrangThai()
                ))
                .toList();
    }
}