package com.javaweb.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TAI_XE")
public class TaiXe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTaiXe")
    private Integer maTaiXe;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    // Quan hệ nhiều tài xế thuộc một nhà xe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhaXe")
    private NhaXe nhaXe;

    // Quan hệ một tài xế có thể phụ trách nhiều xe
    @OneToMany(mappedBy = "taiXe")
    private List<Xe> danhSachXe = new ArrayList<>();

    public TaiXe() {
    }

    public Integer getMaTaiXe() {
        return maTaiXe;
    }

    public void setMaTaiXe(Integer maTaiXe) {
        this.maTaiXe = maTaiXe;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public NhaXe getNhaXe() {
        return nhaXe;
    }

    public void setNhaXe(NhaXe nhaXe) {
        this.nhaXe = nhaXe;
    }

    public List<Xe> getDanhSachXe() {
        return danhSachXe;
    }

    public void setDanhSachXe(List<Xe> danhSachXe) {
        this.danhSachXe = danhSachXe;
    }
}