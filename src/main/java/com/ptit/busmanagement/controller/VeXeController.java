package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.DatVeRequest;
import com.ptit.busmanagement.dto.response.ApiResponse;
import com.ptit.busmanagement.dto.response.SoDoGheResponse;
import com.ptit.busmanagement.entity.VeXe;
import com.ptit.busmanagement.service.VeXeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ve-xe")
@RequiredArgsConstructor
public class VeXeController {
    private final VeXeService service;

    @GetMapping("/so-do-ghe/{maChuyen}")
    public SoDoGheResponse getSoDoGhe(@PathVariable Long maChuyen) {
        return service.getSoDoGhe(maChuyen);
    }

    @PostMapping("/dat-ve")
    public ResponseEntity<ApiResponse<List<VeXe>>> datVe(@Valid @RequestBody DatVeRequest r) {
        List<VeXe> ves = service.datVe(r);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đặt vé thành công", ves));
    }

    @GetMapping("/tra-cuu")
    public List<VeXe> traCuu(@RequestParam String q) {
        return service.traCuuVe(q);
    }

    @GetMapping
    public List<VeXe> list(@RequestParam(required = false) Long maChuyen) {
        return maChuyen != null ? service.findByMaChuyen(maChuyen) : service.findAll();
    }

    @PatchMapping("/{id}/huy")
    public ApiResponse<VeXe> huyVe(@PathVariable Long id) {
        VeXe v = service.huyVe(id);
        return ApiResponse.ok("Hủy vé thành công", v);
    }
}
