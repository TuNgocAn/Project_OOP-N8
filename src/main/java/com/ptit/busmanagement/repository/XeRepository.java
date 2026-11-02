package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.Xe;
import com.ptit.busmanagement.entity.TrangThaiXe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface XeRepository extends JpaRepository<Xe, Long> {
    boolean existsByBienSo(String bienSo);
    boolean existsByBienSoAndMaXeNot(String bienSo, Long maXe);
    boolean existsByMaNhaXe(Long maNhaXe);
    List<Xe> findByMaNhaXe(Long maNhaXe);
    List<Xe> findByTrangThai(TrangThaiXe trangThai);
    List<Xe> findByMaNhaXeAndTrangThai(Long maNhaXe, TrangThaiXe trangThai);
    long countByTrangThai(TrangThaiXe trangThai);
    long countByMaNhaXeAndTrangThai(Long maNhaXe, TrangThaiXe trangThai);
}
