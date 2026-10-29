package com.javaweb.model;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateProfileRequest {

    @NotBlank(message = "Hãy nhập họ và tên.")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự.")
    private String hoTen;

    @NotBlank(message = "Hãy nhập email.")
    @Email(message = "Email chưa đúng định dạng.")
    @Size(max = 100, message = "Email tối đa 100 ký tự.")
    private String email;

    @Pattern(regexp = "^$|^(0|\\+84)\\d{9}$", message = "Số điện thoại gồm 10 số, bắt đầu bằng 0 (hoặc +84).")
    private String soDienThoai;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự.")
    private String diaChi;

    @Past(message = "Ngày sinh phải trước hôm nay.")
    private LocalDate ngaySinh;

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
