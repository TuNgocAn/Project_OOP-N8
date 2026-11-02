package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.ChuyenXeRequest;
import com.ptit.busmanagement.entity.ChuyenXe;
import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import com.ptit.busmanagement.service.ChuyenXeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chuyen-xe")
@RequiredArgsConstructor
public class ChuyenXeController {
    private final ChuyenXeService service;

    @PostMapping
    public ResponseEntity<ChuyenXe> create(@Valid @RequestBody ChuyenXeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @GetMapping
    public List<ChuyenXe> list(
            @RequestParam(required = false) Long maNhaXe,
            @RequestParam(required = false) TrangThaiChuyenXe trangThai) {
        return service.findAll(maNhaXe, trangThai);
    }

    @GetMapping("/search")
    public List<ChuyenXe> search(
            @RequestParam(required = false) String diemDi,
            @RequestParam(required = false) String diemDen,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDi) {
        return service.search(diemDi, diemDen, ngayDi);
    }

    @GetMapping("/{id}")
    public ChuyenXe get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ChuyenXe update(@PathVariable Long id, @Valid @RequestBody ChuyenXeRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/trang-thai")
    public ChuyenXe updateTrangThai(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String ttStr = body.get("trangThai");
        TrangThaiChuyenXe tt = TrangThaiChuyenXe.valueOf(ttStr);
        return service.updateTrangThai(id, tt);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
