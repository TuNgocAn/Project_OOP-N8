package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {
    List<DanhGia> findByMaChuyen(Long maChuyen);
    List<DanhGia> findByMaNhaXe(Long maNhaXe);
}
