package com.bustrack.repository;

import com.bustrack.model.user.User;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;

/**
 * Repository lưu trữ và truy vấn Người dùng (Khách hàng & Nhà xe)
 */
public class UserRepository extends AbstractJsonRepository<User> {
    private static final String FILE_PATH = "data/users.json";

    public UserRepository() {
        super(FILE_PATH, new TypeToken<List<User>>() {}.getType());
    }

    public UserRepository(String customPath) {
        super(customPath, new TypeToken<List<User>>() {}.getType());
    }

    @Override
    protected String getId(User entity) {
        return entity.getId();
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return items.stream()
                .filter(u -> username.equalsIgnoreCase(u.getUsername()))
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}
