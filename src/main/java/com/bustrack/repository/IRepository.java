package com.bustrack.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface chung cho tầng lưu trữ dữ liệu (OOP: Generic, Repository Pattern, Abstraction)
 */
public interface IRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    boolean deleteById(ID id);
    boolean update(T entity);
    void reload();
}
