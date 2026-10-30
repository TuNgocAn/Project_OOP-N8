package TheoDoiChuyenXe.service;

import TheoDoiChuyenXe.dto.ChuyenXeDTO;
import TheoDoiChuyenXe.dto.ChuyenXeRequest;
import TheoDoiChuyenXe.entity.ChuyenXe;
import TheoDoiChuyenXe.entity.LichTrinh;
import TheoDoiChuyenXe.entity.NhaXe;
import TheoDoiChuyenXe.entity.TaiXe;
import TheoDoiChuyenXe.entity.Xe;
import TheoDoiChuyenXe.exception.APIException;
import TheoDoiChuyenXe.repository.ChuyenXeRepository;
import TheoDoiChuyenXe.repository.LichTrinhRepository;
import TheoDoiChuyenXe.repository.NhaXeRepository;
import TheoDoiChuyenXe.repository.TaiXeRepository;
import TheoDoiChuyenXe.repository.XeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class ChuyenXeService {

    // gia dinh
    public static final String CHUA_CHAY = "ChuaChay";
    public static final String DANG_CHAY = "DangChay";
    public static final String HOAN_THANH = "HoanThanh";
    public static final String HUY = "Huy";
    private static final Set<String> TRANG_THAI_HOP_LE = Set.of(CHUA_CHAY, DANG_CHAY, HOAN_THANH, HUY);
    private static final String XE_BAO_TRI = "BaoTri"; // hỏi Người 2 giá trị thật

    private final ChuyenXeRepository chuyenXeRepository;
    private final XeRepository xeRepository;
    private final TaiXeRepository taiXeRepository;
    private final NhaXeRepository nhaXeRepository;
    private final LichTrinhRepository lichTrinhRepository;

    public ChuyenXeService(ChuyenXeRepository chuyenXeRepository, XeRepository xeRepository, TaiXeRepository taiXeRepository,
                           NhaXeRepository nhaXeRepository, LichTrinhRepository lichTrinhRepository) {
        this.chuyenXeRepository = chuyenXeRepository;
        this.xeRepository = xeRepository;
        this.taiXeRepository = taiXeRepository;
        this.nhaXeRepository = nhaXeRepository;
        this.lichTrinhRepository = lichTrinhRepository;
    }

    @Transactional(readOnly = true)
    public List<ChuyenXeDTO> layTatCa() {
        return chuyenXeRepository.findAll().stream().map(this::chuyenSangDTO).toList();
    }

    @Transactional(readOnly = true)
    public ChuyenXeDTO layTheoId(Integer id) {
        return chuyenSangDTO(timChuyen(id));
    }

    @Transactional
    public ChuyenXeDTO tao(ChuyenXeRequest req) {
        if (req.getNgayKhoiHanh().isBefore(LocalDate.now())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Ngày khởi hành không được nằm ở trước");
        }
        ChuyenXe cx = new ChuyenXe();
        cx.setTrangThai(CHUA_CHAY);
        apDungRequest(cx, req, -1);
        return chuyenSangDTO(chuyenXeRepository.save(cx));
    }

    @Transactional
    public ChuyenXeDTO capNhat(Integer id, ChuyenXeRequest req) {
        ChuyenXe cx = timChuyen(id);
        if (HOAN_THANH.equals(cx.getTrangThai()) || HUY.equals(cx.getTrangThai())) {
            throw new APIException(HttpStatus.CONFLICT, "Chuyến đã hoàn thành hoặc đã hủy, không thể chỉnh sửa");
        }
        apDungRequest(cx, req, id);
        return chuyenSangDTO(chuyenXeRepository.save(cx));
    }

    @Transactional
    public void xoa(Integer id) {
        ChuyenXe cx = timChuyen(id);
        if (!CHUA_CHAY.equals(cx.getTrangThai())) {
            throw new APIException(HttpStatus.CONFLICT,
                    "Chỉ được xóa chuyến chưa chạy. Muốn dừng chuyến, hãy đổi trạng thái sang Huy");
        }
        chuyenXeRepository.delete(cx);
    }

    //Inline Function

    private void apDungRequest(ChuyenXe cx, ChuyenXeRequest req, Integer boQuaMaChuyen) {
        NhaXe nhaXe = nhaXeRepository.findById(req.getMaNhaXe())
                .orElseThrow(() -> khongTim("nhà xe", req.getMaNhaXe()));
        Xe xe = xeRepository.findById(req.getMaXe())
                .orElseThrow(() -> khongTim("xe", req.getMaXe()));
        TaiXe taiXe = taiXeRepository.findById(req.getMaTaiXe())
                .orElseThrow(() -> khongTim("tài xế", req.getMaTaiXe()));
        LichTrinh lichTrinh = lichTrinhRepository.findById(req.getMaLichTrinh())
                .orElseThrow(() -> khongTim("lịch trình", req.getMaLichTrinh()));

        // xe, tai xe cung nha xe voi chuyen
        if (xe.getNhaXe() == null || !nhaXe.getMaNhaXe().equals(xe.getNhaXe().getMaNhaXe())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Xe không thuộc nhà xe đã chọn");
        }
        if (taiXe.getNhaXe() == null || !nhaXe.getMaNhaXe().equals(taiXe.getNhaXe().getMaNhaXe())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Tài xế không thuộc nhà xe đã chọn");
        }
        if (XE_BAO_TRI.equals(xe.getTrangThai())) {
            throw new APIException(HttpStatus.CONFLICT, "Xe đang bảo trì, không thể gán vào chuyến");
        }
        if (req.getGioKhoiHanh().equals(req.getGioDuKienDen())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Giờ đến không được trùng giờ khởi hành");
        }
        if (req.getTrangThai() != null && !TRANG_THAI_HOP_LE.contains(req.getTrangThai())) {
            throw new APIException(HttpStatus.BAD_REQUEST,
                    "Trạng thái không hợp lệ. Chỉ nhận: " + TRANG_THAI_HOP_LE);
        }

        // kiem tra trung lich, tinh ca chuyen qua dem
        LocalDate ngay = req.getNgayKhoiHanh();
        LocalDateTime batDau = LocalDateTime.of(ngay, req.getGioKhoiHanh());
        LocalDateTime ketThuc = tinhKetThuc(ngay, req.getGioKhoiHanh(), req.getGioDuKienDen());
        kiemTraTrungLich("Xe", chuyenXeRepository.timChuyenCuaXe(
                xe.getMaXe(), ngay.minusDays(1), ngay.plusDays(1), boQuaMaChuyen), batDau, ketThuc);
        kiemTraTrungLich("Tài xế", chuyenXeRepository.timChuyenCuaTaiXe(
                taiXe.getMaTaiXe(), ngay.minusDays(1), ngay.plusDays(1), boQuaMaChuyen), batDau, ketThuc);
        cx.setNgayKhoiHanh(ngay);
        cx.setGioKhoiHanh(req.getGioKhoiHanh());
        cx.setGioDuKienDen(req.getGioDuKienDen());
        cx.setGiaVe(req.getGiaVe());
        cx.setNhaXe(nhaXe);
        cx.setXe(xe);
        cx.setTaiXe(taiXe);
        cx.setLichTrinh(lichTrinh);
        if (req.getTrangThai() != null) {
            cx.setTrangThai(req.getTrangThai());
        }
    }

    // xe den vao ngay hom sau neu gio den <= gio di
    private LocalDateTime tinhKetThuc(LocalDate ngay, LocalTime gioDi, LocalTime gioDen) {
        LocalDateTime ketThuc = LocalDateTime.of(ngay, gioDen);
        if (!gioDen.isAfter(gioDi)) {
            ketThuc = ketThuc.plusDays(1);
        }
        return ketThuc;
    }

    private void kiemTraTrungLich(String doiTuong, List<ChuyenXe> cacChuyen, LocalDateTime batDau, LocalDateTime ketThuc) {
        for (ChuyenXe c : cacChuyen) {
            LocalDateTime cBatDau = LocalDateTime.of(c.getNgayKhoiHanh(), c.getGioKhoiHanh());
            LocalDateTime cKetThuc = tinhKetThuc(c.getNgayKhoiHanh(), c.getGioKhoiHanh(), c.getGioDuKienDen());
            if (batDau.isBefore(cKetThuc) && cBatDau.isBefore(ketThuc)) {
                throw new APIException(HttpStatus.CONFLICT,
                        doiTuong + " đã có chuyến khác trùng thời gian (mã chuyến " + c.getMaChuyen() + ")");
            }
        }
    }

    private ChuyenXe timChuyen(Integer id) {
        return chuyenXeRepository.findById(id).orElseThrow(() -> khongTim("chuyến xe", id));
    }

    private APIException khongTim(String ten, Integer ma) {
        return new APIException(HttpStatus.NOT_FOUND, "Không tìm thấy " + ten + " có mã " + ma);
    }

    private ChuyenXeDTO chuyenSangDTO(ChuyenXe cx) {
        return new ChuyenXeDTO(
                cx.getMaChuyen(),
                cx.getLichTrinh() != null ? cx.getLichTrinh().getDiemDi() : null,
                cx.getLichTrinh() != null ? cx.getLichTrinh().getDiemDen() : null,
                cx.getNgayKhoiHanh(),
                cx.getGioKhoiHanh(),
                cx.getGioDuKienDen(),
                cx.getGiaVe(),
                cx.getNhaXe() != null ? cx.getNhaXe().getTenNhaXe() : null,
                cx.getTaiXe() != null ? cx.getTaiXe().getHoTen() : null,
                cx.getXe() != null ? cx.getXe().getBienSo() : null,
                cx.getTrangThai());
    }
}