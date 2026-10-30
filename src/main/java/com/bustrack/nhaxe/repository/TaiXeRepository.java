package vn.busapp.nhaxe.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.busapp.nhaxe.entity.TaiXe;

public interface TaiXeRepository extends JpaRepository<TaiXe, Long> {
    boolean existsBySoGPLX(String soGPLX);
    boolean existsBySoGPLXAndMaTaiXeNot(String soGPLX, Long maTaiXe);
    boolean existsByMaNhaXe(Long maNhaXe);
    List<TaiXe> findByMaNhaXe(Long maNhaXe);
}
