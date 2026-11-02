package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.TuyenXe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TuyenXeRepository extends JpaRepository<TuyenXe, Long> {
    List<TuyenXe> findByDiemDiContainingIgnoreCaseAndDiemDenContainingIgnoreCase(String diemDi, String diemDen);
}
