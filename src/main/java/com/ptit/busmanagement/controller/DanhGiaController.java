package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.DanhGiaRequest;
import com.ptit.busmanagement.dto.response.ApiResponse;
import com.ptit.busmanagement.entity.DanhGia;
import com.ptit.busmanagement.service.DanhGiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/danh-gia")
@RequiredArgsConstructor
public class DanhGiaController {
    private final DanhGiaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DanhGia>> create(@Valid @RequestBody DanhGiaRequest r) {
        DanhGia dg = service.danhGia(r);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cảm ơn bạn đã gửi đánh giá!", dg));
    }

    @GetMapping
    public List<DanhGia> list(
            @RequestParam(required = false) Long maChuyen,
            @RequestParam(required = false) Long maNhaXe) {
        if (maChuyen != null) return service.findByChuyenXe(maChuyen);
        if (maNhaXe != null) return service.findByNhaXe(maNhaXe);
        return service.findAll();
    }
}
