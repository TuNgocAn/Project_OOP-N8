package vn.busapp.nhaxe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.busapp.nhaxe.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {}
