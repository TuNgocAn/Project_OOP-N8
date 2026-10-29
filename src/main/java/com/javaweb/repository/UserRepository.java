package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.repository.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Kiểm tra email đã bị người KHÁC dùng chưa (dùng khi cập nhật hồ sơ)
    boolean existsByEmailAndMaUserNot(String email, Integer maUser);
}
