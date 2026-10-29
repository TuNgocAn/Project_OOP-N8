package com.javaweb.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaweb.exception.ApiException;
import com.javaweb.model.LoginRequest;
import com.javaweb.model.RegisterRequest;
import com.javaweb.model.UserInfoResponse;
import com.javaweb.repository.KhachHangRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.entity.KhachHang;
import com.javaweb.repository.entity.User;

@Service
public class AuthService {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_LOCKED = "LOCKED";

    private final UserRepository userRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, KhachHangRepository khachHangRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.khachHangRepository = khachHangRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Đăng ký: tạo User + KhachHang trong cùng một transaction. */
    @Transactional
    public UserInfoResponse register(RegisterRequest req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            Map<String, String> errors = new LinkedHashMap<>();
            errors.put("confirmPassword", "Mật khẩu xác nhận chưa khớp.");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Dữ liệu chưa hợp lệ, vui lòng kiểm tra lại.", errors);
        }

        String username = req.getUsername().trim();
        String email = req.getEmail().trim().toLowerCase();

        Map<String, String> conflicts = new LinkedHashMap<>();
        if (userRepository.existsByUsername(username)) {
            conflicts.put("username", "Tên đăng nhập này đã có người dùng.");
        }
        if (userRepository.existsByEmail(email)) {
            conflicts.put("email", "Email này đã được đăng ký.");
        }
        if (!conflicts.isEmpty()) {
            throw new ApiException(HttpStatus.CONFLICT, "Tên đăng nhập hoặc email đã được sử dụng.", conflicts);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(email);
        user.setSoDienThoai(blankToNull(req.getSoDienThoai()));
        user.setTrangThai(STATUS_ACTIVE);
        userRepository.save(user);

        KhachHang khachHang = new KhachHang();
        khachHang.setUser(user);
        khachHang.setHoTen(req.getHoTen().trim());
        khachHang.setDiaChi(blankToNull(req.getDiaChi()));
        khachHang.setNgaySinh(req.getNgaySinh());
        khachHangRepository.save(khachHang);

        return toInfo(user);
    }

    /** Đăng nhập bằng username hoặc email. Sai thông tin → luôn trả cùng một thông báo. */
    @Transactional(readOnly = true)
    public UserInfoResponse login(LoginRequest req) {
        String identifier = req.getUsername().trim();
        User user = userRepository.findByUsername(identifier).orElse(null);
        if (user == null) {
            user = userRepository.findByEmail(identifier.toLowerCase()).orElse(null);
        }
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Tên đăng nhập hoặc mật khẩu chưa đúng.");
        }
        checkNotLocked(user);
        return toInfo(user);
    }

    /** Thông tin người đang đăng nhập (dùng cho /me). */
    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Tài khoản không còn tồn tại."));
        checkNotLocked(user);
        return toInfo(user);
    }

    private void checkNotLocked(User user) {
        if (STATUS_LOCKED.equals(user.getTrangThai())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Tài khoản của bạn đang bị khóa. Hãy liên hệ quản trị viên.");
        }
    }

    private UserInfoResponse toInfo(User user) {
        String hoTen = khachHangRepository.findByUser_MaUser(user.getMaUser()).map(KhachHang::getHoTen).orElse(null);
        return new UserInfoResponse(user.getMaUser(), user.getUsername(), user.getEmail(), hoTen);
    }

    static String blankToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
