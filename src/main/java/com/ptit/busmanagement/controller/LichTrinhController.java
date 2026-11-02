package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.entity.LichTrinh;
import com.ptit.busmanagement.repository.LichTrinhRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST API Quản lý Lịch trình (Mục 4 & 7).
 */
@RestController
@RequestMapping("/api/lich-trinh")
@RequiredArgsConstructor
public class LichTrinhController {
    private final LichTrinhRepository repo;

    @GetMapping
    public List<LichTrinh> list(
            @RequestParam(required = false) String diemDi,
            @RequestParam(required = false) String diemDen) {
        if (diemDi != null && diemDen != null) {
            return repo.findByDiemDiContainingIgnoreCaseAndDiemDenContainingIgnoreCase(diemDi.trim(), diemDen.trim());
        }
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public LichTrinh get(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<LichTrinh> create(@RequestBody LichTrinh lt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(lt));
    }
}
