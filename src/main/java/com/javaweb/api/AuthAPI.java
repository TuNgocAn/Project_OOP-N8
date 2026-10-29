package com.javaweb.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.ApiResponse;
import com.javaweb.model.LoginRequest;
import com.javaweb.model.RegisterRequest;
import com.javaweb.model.UserInfoResponse;
import com.javaweb.service.AuthService;
import com.javaweb.utils.SessionUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {

    private final AuthService authService;

    public AuthAPI(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserInfoResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserInfoResponse info = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Đăng ký thành công.", info));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserInfoResponse>> login(@Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        UserInfoResponse info = authService.login(request);
        SessionUtils.login(httpRequest, info.getMaUser());
        return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công.", info));
    }

    // POST /api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest) {
        SessionUtils.logout(httpRequest);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Đã đăng xuất.", null));
    }

    // GET /api/auth/me  → ai đang đăng nhập? (401 nếu chưa)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> me(HttpServletRequest httpRequest) {
        Integer userId = SessionUtils.requireUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("OK", authService.getCurrentUser(userId)));
    }
}
