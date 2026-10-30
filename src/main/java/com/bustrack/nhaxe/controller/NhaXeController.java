package vn.busapp.nhaxe.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import vn.busapp.nhaxe.dto.NhaXeRequest;
import vn.busapp.nhaxe.entity.NhaXe;
import vn.busapp.nhaxe.service.NhaXeService;

@RestController @RequestMapping("/api/nha-xe") @RequiredArgsConstructor
public class NhaXeController {
    private final NhaXeService service;

    @PostMapping
    public ResponseEntity<NhaXe> create(@Valid @RequestBody NhaXeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }
    @GetMapping public List<NhaXe> list() { return service.findAll(); }
    @GetMapping("/{id}") public NhaXe get(@PathVariable Long id) { return service.get(id); }
    @PutMapping("/{id}") public NhaXe update(@PathVariable Long id, @Valid @RequestBody NhaXeRequest r) { return service.update(id, r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
