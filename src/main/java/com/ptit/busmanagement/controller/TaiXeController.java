package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.TaiXeRequest;
import com.ptit.busmanagement.dto.request.TaiXeTrangThaiRequest;
import com.ptit.busmanagement.entity.TaiXe;
import com.ptit.busmanagement.entity.TrangThaiTaiXe;
import com.ptit.busmanagement.service.TaiXeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tai-xe")
@RequiredArgsConstructor
public class TaiXeController {
    private final TaiXeService service;

    @PostMapping
    public ResponseEntity<TaiXe> create(@Valid @RequestBody TaiXeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @GetMapping
    public List<TaiXe> list(
            @RequestParam(required = false) Long maNhaXe,
            @RequestParam(required = false) TrangThaiTaiXe trangThai,
            @RequestParam(required = false) String q) {
        return service.find(maNhaXe, trangThai, q);
    }

    @GetMapping("/{id}")
    public TaiXe get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public TaiXe update(@PathVariable Long id, @Valid @RequestBody TaiXeRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/trang-thai")
    public TaiXe updateTrangThai(@PathVariable Long id, @Valid @RequestBody TaiXeTrangThaiRequest r) {
        return service.updateTrangThai(id, r.trangThai());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
