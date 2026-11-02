package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.request.NhaXeRequest;
import com.ptit.busmanagement.entity.NhaXe;
import com.ptit.busmanagement.service.NhaXeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nha-xe")
@RequiredArgsConstructor
public class NhaXeController {
    private final NhaXeService service;

    @PostMapping
    public ResponseEntity<NhaXe> create(@Valid @RequestBody NhaXeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @GetMapping
    public List<NhaXe> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public NhaXe get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public NhaXe update(@PathVariable Long id, @Valid @RequestBody NhaXeRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/status")
    public NhaXe updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "HOAT_DONG");
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
