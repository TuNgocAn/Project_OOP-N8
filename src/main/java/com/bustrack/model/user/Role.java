package com.bustrack.model.user;

/**
 * Vai trò người dùng trong hệ thống
 */
public enum Role {
    CUSTOMER("Khách hàng"),
    OPERATOR("Nhà xe");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
