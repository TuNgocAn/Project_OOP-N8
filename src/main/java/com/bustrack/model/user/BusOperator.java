package com.bustrack.model.user;

/**
 * Lớp đại diện cho Nhà xe quản lý đội xe và chuyến xe (OOP: Inheritance)
 */
public class BusOperator extends User {
    private String companyName;    // Tên nhà xe (ví dụ: Nhà xe Hoàng Long, Nhà xe Phương Trang...)
    private String hotline;        // Hotline liên hệ
    private String officeAddress;  // Địa chỉ văn phòng / bến đỗ chính

    public BusOperator() {
        super();
        setRole(Role.OPERATOR);
    }

    public BusOperator(String id, String username, String password, String fullName, String phoneNumber, 
                       String companyName, String hotline, String officeAddress) {
        super(id, username, password, fullName, phoneNumber, Role.OPERATOR);
        this.companyName = companyName;
        this.hotline = hotline;
        this.officeAddress = officeAddress;
    }

    @Override
    public String getRoleDescription() {
        return "Nhà xe: " + companyName + " (Hotline: " + hotline + ")";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
}
