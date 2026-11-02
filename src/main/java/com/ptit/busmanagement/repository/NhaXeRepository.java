package com.ptit.busmanagement.repository;

import com.ptit.busmanagement.entity.NhaXe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NhaXeRepository extends JpaRepository<NhaXe, Long> {
    List<NhaXe> findByTrangThai(String trangThai);
}
