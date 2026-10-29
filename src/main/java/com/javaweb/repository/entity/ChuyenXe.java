package com.javaweb.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CHUYEN_XE")
public class ChuyenXe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChuyen")
    private Integer maChuyen;

    @Column(name = "NgayKhoiHanh")
    private LocalDate ngayKhoiHanh;

    @Column(name = "GioKhoiHanh")
    private LocalTime gioKhoiHanh;

    @Column(name = "GioDuKienDen")
    private LocalTime gioDuKienDen;

    @Column(name = "GiaVe")
    private BigDecimal giaVe;

    @Column(name = "TrangThai")
    private String trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhaXe")
    private NhaXe nhaXe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaXe")
    private Xe xe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiXe")
    private TaiXe taiXe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLichTrinh")
    private LichTrinh lichTrinh;

    public ChuyenXe() {
    }

    public Integer getMaChuyen() {
        return maChuyen;
    }

    public void setMaChuyen(Integer maChuyen) {
        this.maChuyen = maChuyen;
    }

    public LocalDate getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public void setNgayKhoiHanh(LocalDate ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public LocalTime getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public void setGioKhoiHanh(LocalTime gioKhoiHanh) {
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public LocalTime getGioDuKienDen() {
        return gioDuKienDen;
    }

    public void setGioDuKienDen(LocalTime gioDuKienDen) {
        this.gioDuKienDen = gioDuKienDen;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public NhaXe getNhaXe() {
        return nhaXe;
    }

    public void setNhaXe(NhaXe nhaXe) {
        this.nhaXe = nhaXe;
    }

    public Xe getXe() {
        return xe;
    }

    public void setXe(Xe xe) {
        this.xe = xe;
    }

    public TaiXe getTaiXe() {
        return taiXe;
    }

    public void setTaiXe(TaiXe taiXe) {
        this.taiXe = taiXe;
    }

    public LichTrinh getLichTrinh() {
        return lichTrinh;
    }

    public void setLichTrinh(LichTrinh lichTrinh) {
        this.lichTrinh = lichTrinh;
    }
}