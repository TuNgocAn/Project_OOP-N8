package com.bustrack.repository;

import com.bustrack.util.GsonUtils;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Lớp trừu tượng quản lý việc lưu trữ và đọc ghi dữ liệu dạng JSON (Repository Pattern)
 */
public abstract class AbstractJsonRepository<T> implements IRepository<T, String> {
    private final String filePath;
    private final Type listType;
    protected final List<T> items = new ArrayList<>();
    protected final Gson gson = GsonUtils.getGson();

    public AbstractJsonRepository(String filePath, Type listType) {
        this.filePath = filePath;
        this.listType = listType;
        initStorage();
        loadFromFile();
    }

    /**
     * Phương thức trừu tượng lấy ID của thực thể
     */
    protected abstract String getId(T entity);

    private void initStorage() {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.writeString(path, "[]", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khởi tạo tệp lưu trữ: " + filePath + " - " + e.getMessage());
        }
    }

    protected synchronized void loadFromFile() {
        items.clear();
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            List<T> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                items.addAll(loaded);
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc dữ liệu từ: " + filePath + " - " + e.getMessage());
        }
    }

    @Override
    public synchronized void reload() {
        loadFromFile();
    }

    protected synchronized void saveToFile() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            gson.toJson(items, listType, writer);
        } catch (IOException e) {
            System.err.println("Lỗi ghi dữ liệu ra: " + filePath + " - " + e.getMessage());
        }
    }

    @Override
    public synchronized List<T> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public synchronized Optional<T> findById(String id) {
        if (id == null) return Optional.empty();
        return items.stream()
                .filter(item -> id.equals(getId(item)))
                .findFirst();
    }

    @Override
    public synchronized T save(T entity) {
        if (entity == null) return null;
        String id = getId(entity);
        items.removeIf(item -> id != null && id.equals(getId(item)));
        items.add(entity);
        saveToFile();
        return entity;
    }

    @Override
    public synchronized boolean deleteById(String id) {
        if (id == null) return false;
        boolean removed = items.removeIf(item -> id.equals(getId(item)));
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    @Override
    public synchronized boolean update(T entity) {
        if (entity == null) return false;
        String id = getId(entity);
        for (int i = 0; i < items.size(); i++) {
            if (id != null && id.equals(getId(items.get(i)))) {
                items.set(i, entity);
                saveToFile();
                return true;
            }
        }
        return false;
    }
}
