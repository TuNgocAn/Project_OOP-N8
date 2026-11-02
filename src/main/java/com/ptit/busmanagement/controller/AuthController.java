package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.LoginRequest;
import com.ptit.busmanagement.dto.request.RegisterRequest;
import com.ptit.busmanagement.dto.response.ApiResponse;
import com.ptit.busmanagement.entity.User;
import com.ptit.busmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<User> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        return ApiResponse.ok("Đăng nhập thành công", user);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đăng ký tài khoản thành công", user));
    }
}
