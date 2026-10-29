package com.javaweb.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NHA_XE")
public class NhaXe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNhaXe")
    private Integer maNhaXe;

    @OneToOne
    @JoinColumn(name = "MaUser", nullable = false, unique = true)
    private User user;

    @Column(name = "TenNhaXe")
    private String tenNhaXe;

    @Column(name = "DiaChi")
    private String diaChi;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "MoTa")
    private String moTa;

    public NhaXe() {
    }

    public Integer getMaNhaXe() {
        return maNhaXe;
    }

    public void setMaNhaXe(Integer maNhaXe) {
        this.maNhaXe = maNhaXe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTenNhaXe() {
        return tenNhaXe;
    }

    public void setTenNhaXe(String tenNhaXe) {
        this.tenNhaXe = tenNhaXe;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}