package TheoDoiChuyenXe.service;

import TheoDoiChuyenXe.dto.LichTrinhRequest;
import TheoDoiChuyenXe.entity.LichTrinh;
import TheoDoiChuyenXe.exception.APIException;
import TheoDoiChuyenXe.repository.ChuyenXeRepository;
import TheoDoiChuyenXe.repository.LichTrinhRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LichTrinhService {

    private final LichTrinhRepository lichTrinhRepository;
    private final ChuyenXeRepository chuyenXeRepository;

    public LichTrinhService(LichTrinhRepository lichTrinhRepository,
                            ChuyenXeRepository chuyenXeRepository) {
        this.lichTrinhRepository = lichTrinhRepository;
        this.chuyenXeRepository = chuyenXeRepository;
    }

    @Transactional(readOnly = true)
    public List<LichTrinh> layTatCa() {
        return lichTrinhRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LichTrinh layTheoId(Integer id) {
        return timLichTrinh(id);
    }

    @Transactional
    public LichTrinh tao(LichTrinhRequest req) {
        String diemDi = req.getDiemDi().trim();
        String diemDen = req.getDiemDen().trim();
        kiemTraDiem(diemDi, diemDen);
        if (lichTrinhRepository.existsByDiemDiIgnoreCaseAndDiemDenIgnoreCase(diemDi, diemDen)) {
            throw new APIException(HttpStatus.CONFLICT, "Tuyến " + diemDi + " - " + diemDen + " đã tồn tại");
        }
        LichTrinh lt = new LichTrinh();
        apDung(lt, diemDi, diemDen, req);
        return lichTrinhRepository.save(lt);
    }

    @Transactional
    public LichTrinh capNhat(Integer id, LichTrinhRequest req) {
        LichTrinh lt = timLichTrinh(id);
        String diemDi = req.getDiemDi().trim();
        String diemDen = req.getDiemDen().trim();
        kiemTraDiem(diemDi, diemDen);
        if (lichTrinhRepository.existsByDiemDiIgnoreCaseAndDiemDenIgnoreCaseAndMaLichTrinhNot(
                diemDi, diemDen, id)) {
            throw new APIException(HttpStatus.CONFLICT, "Tuyến " + diemDi + " - " + diemDen + " đã tồn tại");
        }
        apDung(lt, diemDi, diemDen, req);
        return lichTrinhRepository.save(lt);
    }

    @Transactional
    public void xoa(Integer id) {
        LichTrinh lt = timLichTrinh(id);
        if (chuyenXeRepository.existsByLichTrinh_MaLichTrinh(id)) {
            throw new APIException(HttpStatus.CONFLICT,
                    "Lịch trình đang được dùng bởi chuyến xe, không thể xóa");
        }
        lichTrinhRepository.delete(lt);
    }

    // ---------- Hàm nội bộ ----------

    private void kiemTraDiem(String diemDi, String diemDen) {
        if (diemDi.equalsIgnoreCase(diemDen)) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Điểm đi và điểm đến không được trùng nhau");
        }
    }

    private void apDung(LichTrinh lt, String diemDi, String diemDen, LichTrinhRequest req) {
        lt.setDiemDi(diemDi);
        lt.setDiemDen(diemDen);
        lt.setThoiGianDuKien(req.getThoiGianDuKien());
        lt.setKhoangCach(req.getKhoangCach());
    }

    private LichTrinh timLichTrinh(Integer id) {
        return lichTrinhRepository.findById(id).orElseThrow(() ->
                new APIException(HttpStatus.NOT_FOUND, "Không tìm thấy lịch trình có mã " + id));
    }
}