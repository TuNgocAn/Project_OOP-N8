package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.ChuyenXe;
import com.ptit.busmanagement.entity.TrangThaiChuyenXe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ChuyenXeRepository extends JpaRepository<ChuyenXe, Long> {
    List<ChuyenXe> findByMaNhaXe(Long maNhaXe);
    List<ChuyenXe> findByMaXe(Long maXe);
    List<ChuyenXe> findByMaTaiXe(Long maTaiXe);
    List<ChuyenXe> findByMaTuyen(Long maTuyen);
    List<ChuyenXe> findByTrangThai(TrangThaiChuyenXe trangThai);
    List<ChuyenXe> findByKhoiHanhBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByMaXeAndTrangThaiIn(Long maXe, List<TrangThaiChuyenXe> trangThais);
    boolean existsByMaTaiXeAndTrangThaiIn(Long maTaiXe, List<TrangThaiChuyenXe> trangThais);
}
