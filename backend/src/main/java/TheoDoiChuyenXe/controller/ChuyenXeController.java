package TheoDoiChuyenXe.controller;

import TheoDoiChuyenXe.dto.ChuyenXeDTO;
import TheoDoiChuyenXe.dto.ChuyenXeRequest;
import TheoDoiChuyenXe.service.ChuyenXeService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chuyen-xe")
public class ChuyenXeController {

    private final ChuyenXeService chuyenXeService;

    public ChuyenXeController(ChuyenXeService chuyenXeService) {
        this.chuyenXeService = chuyenXeService;
    }

    @GetMapping
    public List<ChuyenXeDTO> layTatCa() {
        return chuyenXeService.layTatCa();
    }

    @GetMapping("/{id}")
    public ChuyenXeDTO layTheoId(@PathVariable Integer id) {
        return chuyenXeService.layTheoId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChuyenXeDTO tao(@Valid @RequestBody ChuyenXeRequest req) {
        return chuyenXeService.tao(req);
    }

    @PutMapping("/{id}")
    public ChuyenXeDTO capNhat(@PathVariable Integer id, @Valid @RequestBody ChuyenXeRequest req) {
        return chuyenXeService.capNhat(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void xoa(@PathVariable Integer id) {
        chuyenXeService.xoa(id);
    }
}