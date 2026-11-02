package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.XeRequest;
import com.ptit.busmanagement.dto.request.XeTrangThaiRequest;
import com.ptit.busmanagement.entity.TrangThaiXe;
import com.ptit.busmanagement.entity.Xe;
import com.ptit.busmanagement.service.XeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/xe")
@RequiredArgsConstructor
public class XeController {
    private final XeService service;

    @PostMapping
    public ResponseEntity<Xe> create(@Valid @RequestBody XeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @GetMapping
    public List<Xe> list(
            @RequestParam(required = false) Long maNhaXe,
            @RequestParam(required = false) TrangThaiXe trangThai,
            @RequestParam(required = false) String q) {
        return service.find(maNhaXe, trangThai, q);
    }

    @GetMapping("/thong-ke")
    public Map<String, Object> thongKe(@RequestParam(required = false) Long maNhaXe) {
        return service.thongKe(maNhaXe);
    }

    @GetMapping("/{id}")
    public Xe get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Xe update(@PathVariable Long id, @Valid @RequestBody XeRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/trang-thai")
    public Xe updateTrangThai(@PathVariable Long id, @Valid @RequestBody XeTrangThaiRequest r) {
        return service.updateTrangThai(id, r.trangThai());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
