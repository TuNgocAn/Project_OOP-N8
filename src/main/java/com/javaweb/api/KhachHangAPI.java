package com.javaweb.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.ApiResponse;
import com.javaweb.model.ProfileResponse;
import com.javaweb.model.UpdateProfileRequest;
import com.javaweb.service.KhachHangService;
import com.javaweb.utils.SessionUtils;

@RestController
@RequestMapping("/api/khach-hang")
public class KhachHangAPI {

    private final KhachHangService khachHangService;

    public KhachHangAPI(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    // GET /api/khach-hang/profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(HttpServletRequest httpRequest) {
        Integer userId = SessionUtils.requireUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("OK", khachHangService.getProfile(userId)));
    }

    // PUT /api/khach-hang/profile
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request, HttpServletRequest httpRequest) {
        Integer userId = SessionUtils.requireUserId(httpRequest);
        ProfileResponse updated = khachHangService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Đã lưu thay đổi.", updated));
    }
}
