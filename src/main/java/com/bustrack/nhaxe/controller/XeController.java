package vn.busapp.nhaxe.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import vn.busapp.nhaxe.dto.*;
import vn.busapp.nhaxe.entity.*;
import vn.busapp.nhaxe.service.XeService;

@RestController @RequestMapping("/api/xe") @RequiredArgsConstructor
public class XeController {
    private final XeService service;

    @PostMapping
    public ResponseEntity<Xe> create(@Valid @RequestBody XeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }
    @GetMapping
    public List<Xe> list(@RequestParam(required = false) Long maNhaXe, @RequestParam(required = false) XeTrangThai trangThai) {
        return service.find(maNhaXe, trangThai);
    }
    @GetMapping("/{id}") public Xe get(@PathVariable Long id) { return service.get(id); }
    @PutMapping("/{id}") public Xe update(@PathVariable Long id, @Valid @RequestBody XeRequest r) { return service.update(id, r); }
    @PatchMapping("/{id}/trang-thai")
    public Xe updateTrangThai(@PathVariable Long id, @Valid @RequestBody XeTrangThaiRequest r) {
        return service.updateTrangThai(id, r.trangThai());
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
