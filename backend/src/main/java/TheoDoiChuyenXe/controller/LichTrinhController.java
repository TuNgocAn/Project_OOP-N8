package TheoDoiChuyenXe.controller;

import TheoDoiChuyenXe.dto.LichTrinhRequest;
import TheoDoiChuyenXe.entity.LichTrinh;
import TheoDoiChuyenXe.service.LichTrinhService;

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
@RequestMapping("/api/lich-trinh")
public class LichTrinhController {

    private final LichTrinhService lichTrinhService;

    public LichTrinhController(LichTrinhService lichTrinhService) {
        this.lichTrinhService = lichTrinhService;
    }

    @GetMapping
    public List<LichTrinh> layTatCa() {
        return lichTrinhService.layTatCa();
    }

    @GetMapping("/{id}")
    public LichTrinh layTheoId(@PathVariable Integer id) {
        return lichTrinhService.layTheoId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LichTrinh tao(@Valid @RequestBody LichTrinhRequest req) {
        return lichTrinhService.tao(req);
    }

    @PutMapping("/{id}")
    public LichTrinh capNhat(@PathVariable Integer id, @Valid @RequestBody LichTrinhRequest req) {
        return lichTrinhService.capNhat(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void xoa(@PathVariable Integer id) {
        lichTrinhService.xoa(id);
    }
}