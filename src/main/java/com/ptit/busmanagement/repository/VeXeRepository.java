package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.VeXe;
import com.ptit.busmanagement.entity.TrangThaiVe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VeXeRepository extends JpaRepository<VeXe, Long> {
    Optional<VeXe> findByMaVeCode(String maVeCode);
    List<VeXe> findByMaChuyen(Long maChuyen);
    List<VeXe> findByMaChuyenAndTrangThai(Long maChuyen, TrangThaiVe trangThai);
    List<VeXe> findBySoDienThoai(String soDienThoai);
    List<VeXe> findByMaKhachHang(Long maKhachHang);
    boolean existsByMaChuyenAndSoGheAndTrangThai(Long maChuyen, String soGhe, TrangThaiVe trangThai);
    long countByTrangThai(TrangThaiVe trangThai);
}
