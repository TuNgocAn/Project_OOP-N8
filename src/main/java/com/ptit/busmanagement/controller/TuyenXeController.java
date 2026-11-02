package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.TuyenXeRequest;
import com.ptit.busmanagement.entity.TuyenXe;
import com.ptit.busmanagement.service.TuyenXeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tuyen-xe")
@RequiredArgsConstructor
public class TuyenXeController {
    private final TuyenXeService service;

    @PostMapping
    public ResponseEntity<TuyenXe> create(@Valid @RequestBody TuyenXeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @GetMapping
    public List<TuyenXe> list(
            @RequestParam(required = false) String diemDi,
            @RequestParam(required = false) String diemDen) {
        return service.search(diemDi, diemDen);
    }

    @GetMapping("/{id}")
    public TuyenXe get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public TuyenXe update(@PathVariable Long id, @Valid @RequestBody TuyenXeRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
