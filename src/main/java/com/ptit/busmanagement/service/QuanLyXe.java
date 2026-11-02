package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.XeRequest;
import com.ptit.busmanagement.entity.TrangThaiXe;
import com.ptit.busmanagement.entity.Xe;
import com.ptit.busmanagement.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Lớp QuanLyXe theo thiết kế OOP (Mục 4 & 7):
 * Xử lý thêm, sửa, xóa, tìm kiếm và cập nhật trạng thái xe (Xe đến, Xe đi, Còn ở bến, Bảo trì).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class QuanLyXe {
    private final XeService xeService;

    public Xe themXe(XeRequest request) {
        return xeService.create(request);
    }

    public Xe suaXe(Long id, XeRequest request) {
        return xeService.update(id, request);
    }

    public void xoaXe(Long id) {
        xeService.delete(id);
    }

    public Xe capNhatTrangThaiXe(Long id, TrangThaiXe trangThai) {
        if (trangThai == null) {
            throw new ValidationException("Trạng thái xe không được để trống");
        }
        return xeService.updateTrangThai(id, trangThai);
    }

    @Transactional(readOnly = true)
    public List<Xe> timKiemXe(Long maNhaXe, TrangThaiXe trangThai, String tuKhoa) {
        return xeService.find(maNhaXe, trangThai, tuKhoa);
    }

    @Transactional(readOnly = true)
    public Xe layChiTietXe(Long id) {
        return xeService.get(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> thongKeXeBenBai(Long maNhaXe) {
        return xeService.thongKe(maNhaXe);
    }
}
