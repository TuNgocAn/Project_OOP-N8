package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.LichTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LichTrinhRepository extends JpaRepository<LichTrinh, Long> {
    List<LichTrinh> findByDiemDiContainingIgnoreCaseAndDiemDenContainingIgnoreCase(String diemDi, String diemDen);
}
