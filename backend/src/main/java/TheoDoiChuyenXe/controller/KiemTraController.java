package TheoDoiChuyenXe.controller;

import TheoDoiChuyenXe.repository.UserRepository;
import TheoDoiChuyenXe.repository.KhachHangRepository;
import TheoDoiChuyenXe.repository.NhaXeRepository;
import TheoDoiChuyenXe.repository.XeRepository;
import TheoDoiChuyenXe.repository.TaiXeRepository;
import TheoDoiChuyenXe.repository.LichTrinhRepository;
import TheoDoiChuyenXe.repository.ChuyenXeRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class KiemTraController {

    private final UserRepository userRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhaXeRepository nhaXeRepository;
    private final XeRepository xeRepository;
    private final TaiXeRepository taiXeRepository;
    private final LichTrinhRepository lichTrinhRepository;
    private final ChuyenXeRepository chuyenXeRepository;

    public KiemTraController(
            UserRepository userRepository,
            KhachHangRepository khachHangRepository,
            NhaXeRepository nhaXeRepository,
            XeRepository xeRepository,
            TaiXeRepository taiXeRepository,
            LichTrinhRepository lichTrinhRepository,
            ChuyenXeRepository chuyenXeRepository) {

        this.userRepository = userRepository;
        this.khachHangRepository = khachHangRepository;
        this.nhaXeRepository = nhaXeRepository;
        this.xeRepository = xeRepository;
        this.taiXeRepository = taiXeRepository;
        this.lichTrinhRepository = lichTrinhRepository;
        this.chuyenXeRepository = chuyenXeRepository;
    }

    @GetMapping("/api/kiem-tra")
    public Map<String, Long> kiemTra() {

        Map<String, Long> ketQua = new LinkedHashMap<>();

        ketQua.put("USER", userRepository.count());
        ketQua.put("KHACH_HANG", khachHangRepository.count());
        ketQua.put("NHA_XE", nhaXeRepository.count());
        ketQua.put("XE", xeRepository.count());
        ketQua.put("TAI_XE", taiXeRepository.count());
        ketQua.put("LICH_TRINH", lichTrinhRepository.count());
        ketQua.put("CHUYEN_XE", chuyenXeRepository.count());

        return ketQua;
    }
}