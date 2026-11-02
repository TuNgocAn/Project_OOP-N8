-- ==============================================================
-- DATABASE SCHEMA CHO HỆ THỐNG QUẢN LÝ BẾN XE KHÁCH
-- ==============================================================

CREATE DATABASE IF NOT EXISTS bus_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bus_management;

DROP TABLE IF EXISTS danh_gia;
DROP TABLE IF EXISTS ve_xe;
DROP TABLE IF EXISTS chuyen_xe;
DROP TABLE IF EXISTS tuyen_xe;
DROP TABLE IF EXISTS tai_xe;
DROP TABLE IF EXISTS xe;
DROP TABLE IF EXISTS nha_xe;
DROP TABLE IF EXISTS khach_hang;
DROP TABLE IF EXISTS users;

-- 1. Bảng USERS (Tài khoản người dùng)
CREATE TABLE users (
    ma_user BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    vai_tro VARCHAR(20) NOT NULL DEFAULT 'KHACH_HANG', -- ADMIN, NHA_XE, KHACH_HANG
    trang_thai VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG', -- HOAT_DONG, KHOA
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng NHA_XE (Nhà xe, quan hệ 1:1 với USERS)
CREATE TABLE nha_xe (
    ma_user BIGINT PRIMARY KEY,
    ten_nha_xe VARCHAR(100) NOT NULL,
    dia_chi VARCHAR(255),
    so_dien_thoai VARCHAR(20),
    mo_ta TEXT,
    trang_thai VARCHAR(20) DEFAULT 'HOAT_DONG',
    FOREIGN KEY (ma_user) REFERENCES users(ma_user) ON DELETE CASCADE
);

-- 3. Bảng KHACH_HANG (Khách hàng, quan hệ 1:1 với USERS)
CREATE TABLE khach_hang (
    ma_user BIGINT PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    dia_chi VARCHAR(255),
    ngay_sinh DATE,
    cccd VARCHAR(20),
    FOREIGN KEY (ma_user) REFERENCES users(ma_user) ON DELETE CASCADE
);

-- 4. Bảng XE (Phương tiện thuộc nhà xe, theo dõi trạng thái bến bãi)
CREATE TABLE xe (
    ma_xe BIGINT AUTO_INCREMENT PRIMARY KEY,
    bien_so VARCHAR(20) NOT NULL UNIQUE,
    loai_xe VARCHAR(50) NOT NULL,
    so_cho INT NOT NULL,
    trang_thai VARCHAR(20) NOT NULL DEFAULT 'CON_O_BEN', -- CON_O_BEN, XE_DEN, XE_DI, BAO_TRI
    ma_nha_xe BIGINT NOT NULL,
    FOREIGN KEY (ma_nha_xe) REFERENCES nha_xe(ma_user) ON DELETE CASCADE
);

-- 5. Bảng TAI_XE (Tài xế thuộc nhà xe)
CREATE TABLE tai_xe (
    ma_tai_xe BIGINT AUTO_INCREMENT PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    so_dien_thoai VARCHAR(20),
    so_gplx VARCHAR(30) NOT NULL UNIQUE,
    loai_bang VARCHAR(10) DEFAULT 'D',
    trang_thai VARCHAR(20) NOT NULL DEFAULT 'RANH', -- RANH, DANG_LAI, NGHI
    ma_nha_xe BIGINT NOT NULL,
    FOREIGN KEY (ma_nha_xe) REFERENCES nha_xe(ma_user) ON DELETE CASCADE
);

-- 6. Bảng TUYEN_XE (Lịch trình tuyến cố định)
CREATE TABLE tuyen_xe (
    ma_tuyen BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_tuyen VARCHAR(100) NOT NULL,
    diem_di VARCHAR(50) NOT NULL,
    diem_den VARCHAR(50) NOT NULL,
    thoi_gian_du_kien_phut INT DEFAULT 120,
    khoang_cach_km DOUBLE DEFAULT 100.0
);

-- 7. Bảng CHUYEN_XE (Chuyến xe khởi hành cụ thể)
CREATE TABLE chuyen_xe (
    ma_chuyen BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nha_xe BIGINT NOT NULL,
    ma_xe BIGINT NOT NULL,
    ma_tai_xe BIGINT NOT NULL,
    ma_tuyen BIGINT NOT NULL,
    khoi_hanh DATETIME NOT NULL,
    du_kien_den DATETIME,
    gia_ve BIGINT NOT NULL,
    trang_thai VARCHAR(20) NOT NULL DEFAULT 'SAP_KHOI_HANH', -- SAP_KHOI_HANH, DANG_CHAY, HOAN_THANH, DA_HUY
    FOREIGN KEY (ma_nha_xe) REFERENCES nha_xe(ma_user),
    FOREIGN KEY (ma_xe) REFERENCES xe(ma_xe),
    FOREIGN KEY (ma_tai_xe) REFERENCES tai_xe(ma_tai_xe),
    FOREIGN KEY (ma_tuyen) REFERENCES tuyen_xe(ma_tuyen)
);

-- 8. Bảng VE_XE (Vé đặt của hành khách)
CREATE TABLE ve_xe (
    ma_ve BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_ve_code VARCHAR(30) NOT NULL UNIQUE,
    ma_chuyen BIGINT NOT NULL,
    ma_khach_hang BIGINT,
    ten_khach_hang VARCHAR(100) NOT NULL,
    so_dien_thoai VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    so_ghe VARCHAR(20) NOT NULL,
    gia_ve BIGINT NOT NULL,
    ngay_dat DATETIME DEFAULT CURRENT_TIMESTAMP,
    trang_thai VARCHAR(20) NOT NULL DEFAULT 'DA_DAT', -- DA_DAT, DA_HUY
    FOREIGN KEY (ma_chuyen) REFERENCES chuyen_xe(ma_chuyen) ON DELETE CASCADE,
    FOREIGN KEY (ma_khach_hang) REFERENCES khach_hang(ma_user)
);

-- 9. Bảng DANH_GIA (Đánh giá nhận xét)
CREATE TABLE danh_gia (
    ma_danh_gia BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_chuyen BIGINT NOT NULL,
    ma_nha_xe BIGINT,
    ma_khach_hang BIGINT,
    ten_khach_hang VARCHAR(100) NOT NULL,
    so_sao INT NOT NULL CHECK (so_sao BETWEEN 1 AND 5),
    noi_dung TEXT,
    ngay_danh_gia DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_chuyen) REFERENCES chuyen_xe(ma_chuyen)
);
