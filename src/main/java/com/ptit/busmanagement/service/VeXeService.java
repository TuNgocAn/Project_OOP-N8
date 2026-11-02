package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.DatVeRequest;
import com.ptit.busmanagement.dto.response.SoDoGheResponse;
import com.ptit.busmanagement.entity.*;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.repository.ChuyenXeRepository;
import com.ptit.busmanagement.repository.VeXeRepository;
import com.ptit.busmanagement.repository.XeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VeXeService {
    private final VeXeRepository repo;
    private final ChuyenXeRepository chuyenXeRepo;
    private final XeRepository xeRepo;
    private final ChuyenXeService chuyenXeService;

    @Transactional(readOnly = true)
    public SoDoGheResponse getSoDoGhe(Long maChuyen) {
        ChuyenXe cx = chuyenXeRepo.findById(maChuyen)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + maChuyen));
        Xe xe = xeRepo.findById(cx.getMaXe()).orElse(null);

        List<String> bookedSeats = repo.findByMaChuyenAndTrangThai(maChuyen, TrangThaiVe.DA_DAT)
                .stream()
                .map(VeXe::getSoGhe)
                .collect(Collectors.toList());

        return SoDoGheResponse.builder()
                .maChuyen(maChuyen)
                .maXe(cx.getMaXe())
                .bienSo(xe != null ? xe.getBienSo() : "N/A")
                .loaiXe(xe != null ? xe.getLoaiXe() : "Tiêu chuẩn")
                .soCho(xe != null ? xe.getSoCho() : 40)
                .giaVe(cx.getGiaVe())
                .danhSachGheDaDat(bookedSeats)
                .build();
    }

    public List<VeXe> datVe(DatVeRequest r) {
        ChuyenXe cx = chuyenXeRepo.findById(r.maChuyen())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chuyến xe ID: " + r.maChuyen()));

        if (cx.getTrangThai() == TrangThaiChuyenXe.DA_HUY || cx.getTrangThai() == TrangThaiChuyenXe.HOAN_THANH) {
            throw new ConflictException("Chuyến xe không còn nhận đặt vé");
        }

        String[] seats = r.soGhe().split(",");
        List<VeXe> dsVe = new ArrayList<>();

        synchronized (this) {
            for (String seatRaw : seats) {
                String seat = seatRaw.trim().toUpperCase();
                if (seat.isEmpty()) continue;

                boolean daDat = repo.existsByMaChuyenAndSoGheAndTrangThai(r.maChuyen(), seat, TrangThaiVe.DA_DAT);
                if (daDat) {
                    throw new ConflictException("Ghế " + seat + " đã có khách đặt trước! Vui lòng chọn ghế khác.");
                }

                String code = "VE" + System.currentTimeMillis() % 1000000 + "-" + seat;
                VeXe v = VeXe.builder()
                        .maVeCode(code)
                        .maChuyen(r.maChuyen())
                        .maKhachHang(r.maKhachHang())
                        .tenKhachHang(r.tenKhachHang().trim())
                        .soDienThoai(r.soDienThoai().trim())
                        .email(r.email())
                        .soGhe(seat)
                        .giaVe(cx.getGiaVe())
                        .ngayDat(LocalDateTime.now())
                        .trangThai(TrangThaiVe.DA_DAT)
                        .build();
                dsVe.add(repo.save(v));
            }
        }

        dsVe.forEach(v -> v.setChuyenXe(chuyenXeService.populate(cx)));
        return dsVe;
    }

    @Transactional(readOnly = true)
    public List<VeXe> traCuuVe(String keyword) {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        String kw = keyword.trim();
        List<VeXe> res = new ArrayList<>();
        repo.findByMaVeCode(kw).ifPresent(res::add);

        List<VeXe> byPhone = repo.findBySoDienThoai(kw);
        for (VeXe v : byPhone) {
            if (res.stream().noneMatch(x -> x.getMaVe().equals(v.getMaVe()))) {
                res.add(v);
            }
        }

        res.forEach(v -> chuyenXeRepo.findById(v.getMaChuyen()).ifPresent(c -> v.setChuyenXe(chuyenXeService.populate(c))));
        return res;
    }

    public VeXe huyVe(Long maVe) {
        VeXe v = repo.findById(maVe).orElseThrow(() -> new NotFoundException("Không tìm thấy vé ID: " + maVe));
        if (v.getTrangThai() == TrangThaiVe.DA_HUY) {
            throw new ConflictException("Vé này đã được hủy trước đó");
        }
        v.setTrangThai(TrangThaiVe.DA_HUY);
        return repo.save(v);
    }

    @Transactional(readOnly = true)
    public List<VeXe> findByMaChuyen(Long maChuyen) {
        List<VeXe> list = repo.findByMaChuyen(maChuyen);
        list.forEach(v -> chuyenXeRepo.findById(v.getMaChuyen()).ifPresent(c -> v.setChuyenXe(chuyenXeService.populate(c))));
        return list;
    }

    @Transactional(readOnly = true)
    public List<VeXe> findAll() {
        List<VeXe> list = repo.findAll();
        list.forEach(v -> chuyenXeRepo.findById(v.getMaChuyen()).ifPresent(c -> v.setChuyenXe(chuyenXeService.populate(c))));
        return list;
    }
}
