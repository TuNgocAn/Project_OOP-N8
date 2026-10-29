package com.javaweb.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaweb.exception.ApiException;
import com.javaweb.model.ProfileResponse;
import com.javaweb.model.UpdateProfileRequest;
import com.javaweb.repository.KhachHangRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.entity.KhachHang;
import com.javaweb.repository.entity.User;

@Service
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final UserRepository userRepository;

    public KhachHangService(KhachHangRepository khachHangRepository, UserRepository userRepository) {
        this.khachHangRepository = khachHangRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Integer userId) {
        return toProfile(findKhachHang(userId));
    }

    @Transactional
    public ProfileResponse updateProfile(Integer userId, UpdateProfileRequest req) {
        KhachHang khachHang = findKhachHang(userId);
        User user = khachHang.getUser();

        String email = req.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailAndMaUserNot(email, user.getMaUser())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email này đã được dùng cho tài khoản khác.",
                    Collections.singletonMap("email", "Email này đã được dùng cho tài khoản khác."));
        }

        user.setEmail(email);
        user.setSoDienThoai(AuthService.blankToNull(req.getSoDienThoai()));
        khachHang.setHoTen(req.getHoTen().trim());
        khachHang.setDiaChi(AuthService.blankToNull(req.getDiaChi()));
        khachHang.setNgaySinh(req.getNgaySinh());

        userRepository.save(user);
        khachHangRepository.save(khachHang);
        return toProfile(khachHang);
    }

    private KhachHang findKhachHang(Integer userId) {
        return khachHangRepository.findByUser_MaUser(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Tài khoản này không phải là khách hàng."));
    }

    private ProfileResponse toProfile(KhachHang kh) {
        User user = kh.getUser();
        ProfileResponse p = new ProfileResponse();
        p.setMaKhachHang(kh.getMaKhachHang());
        p.setMaUser(user.getMaUser());
        p.setUsername(user.getUsername());
        p.setEmail(user.getEmail());
        p.setSoDienThoai(user.getSoDienThoai());
        p.setHoTen(kh.getHoTen());
        p.setDiaChi(kh.getDiaChi());
        p.setNgaySinh(kh.getNgaySinh());
        p.setNgayTao(user.getNgayTao());
        return p;
    }
}
