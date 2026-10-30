package vn.busapp.nhaxe.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.busapp.nhaxe.entity.Xe;
import vn.busapp.nhaxe.entity.XeTrangThai;

public interface XeRepository extends JpaRepository<Xe, Long> {
    boolean existsByBienSo(String bienSo);
    boolean existsByBienSoAndMaXeNot(String bienSo, Long maXe);
    boolean existsByMaNhaXe(Long maNhaXe);
    List<Xe> findByMaNhaXe(Long maNhaXe);
    List<Xe> findByTrangThai(XeTrangThai trangThai);
    List<Xe> findByMaNhaXeAndTrangThai(Long maNhaXe, XeTrangThai trangThai);
}
