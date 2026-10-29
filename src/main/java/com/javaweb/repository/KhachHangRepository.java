package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.repository.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    Optional<KhachHang> findByUser_MaUser(Integer maUser);
}
