package com.bustrack.model.trip;

/**
 * Thông tin tài xế phụ trách chuyến xe
 * Theo yêu cầu: "quản lý tài xế cho chuyến xe (chỉ thêm thông tin tài xế cho mỗi chuyến xe)"
 */
public class Driver {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String licenseNumber;     // Số giấy phép lái xe (GPLX hạng D/E)
    private int yearsOfExperience;    // Số năm kinh nghiệm lái xe đường dài

    public Driver() {
    }

    public Driver(String id, String fullName, String phoneNumber, String licenseNumber, int yearsOfExperience) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String toString() {
        return fullName + " (SĐT: " + phoneNumber + ", GPLX: " + licenseNumber + ")";
    }
}
