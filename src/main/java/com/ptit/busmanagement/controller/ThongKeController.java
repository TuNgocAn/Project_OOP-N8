package com.ptit.busmanagement.controller;

import com.ptit.busmanagement.dto.response.ThongKeBenXeResponse;
import com.ptit.busmanagement.service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thong-ke")
@RequiredArgsConstructor
public class ThongKeController {
    private final ThongKeService service;

    @GetMapping("/ben-xe")
    public ThongKeBenXeResponse thongKeBenXe() {
        return service.thongKeBenXe();
    }
}
