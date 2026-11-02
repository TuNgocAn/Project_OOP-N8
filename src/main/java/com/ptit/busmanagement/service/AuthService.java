package com.ptit.busmanagement.service;

import com.ptit.busmanagement.dto.request.LoginRequest;
import com.ptit.busmanagement.dto.request.RegisterRequest;
import com.ptit.busmanagement.entity.*;
import com.ptit.busmanagement.exception.ConflictException;
import com.ptit.busmanagement.exception.NotFoundException;
import com.ptit.busmanagement.exception.ValidationException;
import com.ptit.busmanagement.repository.KhachHangRepository;
import com.ptit.busmanagement.repository.NhaXeRepository;
import com.ptit.busmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepo;
    private final NhaXeRepository nhaXeRepo;
    private final KhachHangRepository khachHangRepo;

    public User login(LoginRequest r) {
        User u = userRepo.findByUsername(r.username().trim())
                .orElseThrow(() -> new NotFoundException("Tài khoản hoặc mật khẩu không chính xác"));
        if (!u.getPassword().equals(r.password())) {
            throw new ValidationException("Tài khoản hoặc mật khẩu không chính xác");
        }
        if ("KHOA".equalsIgnoreCase(u.getTrangThai())) {
            throw new ValidationException("Tài khoản đã bị tạm khóa, vui lòng liên hệ ban quản lý bến xe");
        }
        return u;
    }

    public User register(RegisterRequest r) {
        String username = r.username().trim();
        if (userRepo.existsByUsername(username)) {
            throw new ConflictException("Tên đăng nhập đã tồn tại: " + username);
        }

        String vaiTro = r.vaiTro() != null && !r.vaiTro().isBlank() ? r.vaiTro().trim().toUpperCase() : "KHACH_HANG";
        User user = User.builder()
                .username(username)
                .password(r.password())
                .email(r.email())
                .soDienThoai(r.soDienThoai())
                .vaiTro(vaiTro)
                .trangThai("HOAT_DONG")
                .build();
        user = userRepo.save(user);

        if ("NHA_XE".equals(vaiTro)) {
            String tenNx = r.tenNhaXe() != null && !r.tenNhaXe().isBlank() ? r.tenNhaXe() : "Nhà xe " + username;
            NhaXe nx = NhaXe.builder()
                    .maUser(user.getMaUser())
                    .tenNhaXe(tenNx)
                    .diaChi(r.diaChi())
                    .soDienThoai(r.soDienThoai())
                    .moTa("Nhà xe đối tác bến xe")
                    .trangThai("HOAT_DONG")
                    .build();
            nhaXeRepo.save(nx);
        } else if ("KHACH_HANG".equals(vaiTro)) {
            String hoTen = r.hoTen() != null && !r.hoTen().isBlank() ? r.hoTen() : username;
            KhachHang kh = KhachHang.builder()
                    .maUser(user.getMaUser())
                    .hoTen(hoTen)
                    .diaChi(r.diaChi())
                    .build();
            khachHangRepo.save(kh);
        }

        return user;
    }
}
