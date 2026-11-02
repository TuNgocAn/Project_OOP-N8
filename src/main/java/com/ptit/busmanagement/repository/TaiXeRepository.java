package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.TaiXe;
import com.ptit.busmanagement.entity.TrangThaiTaiXe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaiXeRepository extends JpaRepository<TaiXe, Long> {
    boolean existsBySoGPLX(String soGPLX);
    boolean existsBySoGPLXAndMaTaiXeNot(String soGPLX, Long maTaiXe);
    boolean existsByMaNhaXe(Long maNhaXe);
    List<TaiXe> findByMaNhaXe(Long maNhaXe);
    List<TaiXe> findByTrangThai(TrangThaiTaiXe trangThai);
    List<TaiXe> findByMaNhaXeAndTrangThai(Long maNhaXe, TrangThaiTaiXe trangThai);
}
