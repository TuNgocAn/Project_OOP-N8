package com.bustrack.model.user;

/**
 * Lớp đại diện cho khách hàng có nhu cầu về quê (OOP: Inheritance)
 */
public class Customer extends User {
    private String email;
    private String hometown; // Quê quán (ví dụ: Nam Định, Nghệ An, Thanh Hóa, Đà Lạt...)

    public Customer() {
        super();
        setRole(Role.CUSTOMER);
    }

    public Customer(String id, String username, String password, String fullName, String phoneNumber, String email, String hometown) {
        super(id, username, password, fullName, phoneNumber, Role.CUSTOMER);
        this.email = email;
        this.hometown = hometown;
    }

    @Override
    public String getRoleDescription() {
        return "Khách hàng (Quê quán: " + (hometown != null && !hometown.isBlank() ? hometown : "Chưa cập nhật") + ")";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }
}
