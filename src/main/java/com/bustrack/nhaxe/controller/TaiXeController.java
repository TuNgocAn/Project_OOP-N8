package vn.busapp.nhaxe.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import vn.busapp.nhaxe.dto.TaiXeRequest;
import vn.busapp.nhaxe.entity.TaiXe;
import vn.busapp.nhaxe.service.TaiXeService;

@RestController @RequestMapping("/api/tai-xe") @RequiredArgsConstructor
public class TaiXeController {
    private final TaiXeService service;

    @PostMapping
    public ResponseEntity<TaiXe> create(@Valid @RequestBody TaiXeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }
    @GetMapping public List<TaiXe> list(@RequestParam(required = false) Long maNhaXe) { return service.find(maNhaXe); }
    @GetMapping("/{id}") public TaiXe get(@PathVariable Long id) { return service.get(id); }
    @PutMapping("/{id}") public TaiXe update(@PathVariable Long id, @Valid @RequestBody TaiXeRequest r) { return service.update(id, r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
