
/*
    DATABASE: TheoDoiChuyenXe
    Phien ban: da bo DANH_GIA
    TAI_XE chi con 4 thuoc tinh
    Dung cho database moi, chua ton tai.
*/

USE [master];
GO

CREATE DATABASE [TheoDoiChuyenXe];
GO

USE [TheoDoiChuyenXe];
GO

/* =========================
   1. TAO CAC BANG
   ========================= */

CREATE TABLE dbo.[USER] (
    MaUser INT IDENTITY(1,1) NOT NULL,
    Username NVARCHAR(50) NOT NULL,
    [Password] NVARCHAR(255) NOT NULL,
    Email NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) NULL,
    NgayTao DATETIME2(7) NOT NULL
        CONSTRAINT DF_USER_NgayTao DEFAULT SYSDATETIME(),
    TrangThai NVARCHAR(30) NOT NULL
        CONSTRAINT DF_USER_TrangThai DEFAULT N'HoatDong',

    CONSTRAINT PK_USER PRIMARY KEY (MaUser),
    CONSTRAINT UQ_USER_Username UNIQUE (Username),
    CONSTRAINT UQ_USER_Email UNIQUE (Email)
);
GO

CREATE TABLE dbo.KHACH_HANG (
    MaUser INT NOT NULL,
    HoTen NVARCHAR(100) NOT NULL,
    DiaChi NVARCHAR(255) NULL,
    NgaySinh DATE NULL,

    CONSTRAINT PK_KHACH_HANG PRIMARY KEY (MaUser),
    CONSTRAINT FK_KHACHHANG_USER
        FOREIGN KEY (MaUser) REFERENCES dbo.[USER](MaUser)
);
GO

CREATE TABLE dbo.NHA_XE (
    MaUser INT NOT NULL,
    TenNhaXe NVARCHAR(150) NOT NULL,
    DiaChi NVARCHAR(255) NULL,
    SoDienThoai VARCHAR(15) NULL,
    MoTa NVARCHAR(500) NULL,

    CONSTRAINT PK_NHA_XE PRIMARY KEY (MaUser),
    CONSTRAINT FK_NHAXE_USER
        FOREIGN KEY (MaUser) REFERENCES dbo.[USER](MaUser)
);
GO

CREATE TABLE dbo.LICH_TRINH (
    MaLichTrinh INT IDENTITY(1,1) NOT NULL,
    DiemDi NVARCHAR(150) NOT NULL,
    DiemDen NVARCHAR(150) NOT NULL,
    ThoiGianDuKien INT NULL,
    KhoangCach DECIMAL(10,2) NULL,

    CONSTRAINT PK_LICH_TRINH PRIMARY KEY (MaLichTrinh),
    CONSTRAINT CK_LICHTRINH_KHOANGCACH
        CHECK (KhoangCach IS NULL OR KhoangCach > 0),
    CONSTRAINT CK_LICHTRINH_THOIGIAN
        CHECK (ThoiGianDuKien IS NULL OR ThoiGianDuKien > 0)
);
GO

CREATE TABLE dbo.TAI_XE (
    MaTaiXe INT IDENTITY(1,1) NOT NULL,
    HoTen NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) NULL,
    MaNhaXe INT NOT NULL,

    CONSTRAINT PK_TAI_XE PRIMARY KEY (MaTaiXe),
    CONSTRAINT FK_TAIXE_NHAXE
        FOREIGN KEY (MaNhaXe) REFERENCES dbo.NHA_XE(MaUser)
);
GO

CREATE TABLE dbo.XE (
    MaXe INT IDENTITY(1,1) NOT NULL,
    BienSo NVARCHAR(20) NOT NULL,
    LoaiXe NVARCHAR(50) NULL,
    SoCho INT NOT NULL,
    TrangThai NVARCHAR(30) NOT NULL
        CONSTRAINT DF_XE_TrangThai DEFAULT N'SanSang',
    MaNhaXe INT NOT NULL,

    CONSTRAINT PK_XE PRIMARY KEY (MaXe),
    CONSTRAINT UQ_XE_BienSo UNIQUE (BienSo),
    CONSTRAINT CK_XE_SOCHO CHECK (SoCho > 0),
    CONSTRAINT FK_XE_NHAXE
        FOREIGN KEY (MaNhaXe) REFERENCES dbo.NHA_XE(MaUser)
);
GO

CREATE TABLE dbo.CHUYEN_XE (
    MaChuyen INT IDENTITY(1,1) NOT NULL,
    NgayKhoiHanh DATE NOT NULL,
    GioKhoiHanh TIME(7) NOT NULL,
    GioDuKienDen TIME(7) NOT NULL,
    GiaVe DECIMAL(12,2) NOT NULL,
    TrangThai NVARCHAR(30) NOT NULL
        CONSTRAINT DF_CHUYENXE_TrangThai DEFAULT N'ChuaChay',
    MaNhaXe INT NOT NULL,
    MaXe INT NOT NULL,
    MaTaiXe INT NOT NULL,
    MaLichTrinh INT NOT NULL,

    CONSTRAINT PK_CHUYEN_XE PRIMARY KEY (MaChuyen),
    CONSTRAINT CK_CHUYENXE_GIAVE CHECK (GiaVe >= 0),

    CONSTRAINT FK_CHUYENXE_NHAXE
        FOREIGN KEY (MaNhaXe) REFERENCES dbo.NHA_XE(MaUser),

    CONSTRAINT FK_CHUYENXE_XE
        FOREIGN KEY (MaXe) REFERENCES dbo.XE(MaXe),

    CONSTRAINT FK_CHUYENXE_TAIXE
        FOREIGN KEY (MaTaiXe) REFERENCES dbo.TAI_XE(MaTaiXe),

    CONSTRAINT FK_CHUYENXE_LICHTRINH
        FOREIGN KEY (MaLichTrinh)
        REFERENCES dbo.LICH_TRINH(MaLichTrinh)
);
GO

/* =========================
   2. THEM DU LIEU MAU
   ========================= */

/* USER */
SET IDENTITY_INSERT dbo.[USER] ON;

INSERT INTO dbo.[USER]
    (MaUser, Username, [Password], Email, SoDienThoai, NgayTao, TrangThai)
VALUES
    (7, N'khach01', N'demo123', N'khach01@example.com',
     '0900000001', '2026-10-11T12:25:14.1847562', N'HoatDong'),
    (8, N'nhaxe01', N'demo123', N'nhaxe01@example.com',
     '0900000002', '2026-10-11T12:25:14.1847562', N'HoatDong');

SET IDENTITY_INSERT dbo.[USER] OFF;
GO

/* KHACH_HANG */
INSERT INTO dbo.KHACH_HANG (MaUser, HoTen, DiaChi, NgaySinh)
VALUES (7, N'Nguyen Van An', N'Ha Noi', '2005-01-15');
GO

/* NHA_XE */
INSERT INTO dbo.NHA_XE (MaUser, TenNhaXe, DiaChi, SoDienThoai, MoTa)
VALUES (8, N'Nha xe Sao Viet', N'Ha Noi',
        '0900000002', N'Chuyen xe duong dai');
GO

/* LICH_TRINH */
SET IDENTITY_INSERT dbo.LICH_TRINH ON;

INSERT INTO dbo.LICH_TRINH
    (MaLichTrinh, DiemDi, DiemDen, ThoiGianDuKien, KhoangCach)
VALUES
    (5, N'Ha Noi', N'Thanh Hoa', 180, 150.00);

SET IDENTITY_INSERT dbo.LICH_TRINH OFF;
GO

/* TAI_XE: chi 4 thuoc tinh */
SET IDENTITY_INSERT dbo.TAI_XE ON;

INSERT INTO dbo.TAI_XE (MaTaiXe, HoTen, SoDienThoai, MaNhaXe)
VALUES (6, N'Tran Van Binh', '0900000003', 8);

SET IDENTITY_INSERT dbo.TAI_XE OFF;
GO

/* XE */
SET IDENTITY_INSERT dbo.XE ON;

INSERT INTO dbo.XE
    (MaXe, BienSo, LoaiXe, SoCho, TrangThai, MaNhaXe)
VALUES
    (6, N'29B-12345', N'Xe khach', 40, N'SanSang', 8);

SET IDENTITY_INSERT dbo.XE OFF;
GO

/* CHUYEN_XE */
SET IDENTITY_INSERT dbo.CHUYEN_XE ON;

INSERT INTO dbo.CHUYEN_XE
    (MaChuyen, NgayKhoiHanh, GioKhoiHanh, GioDuKienDen,
     GiaVe, TrangThai, MaNhaXe, MaXe, MaTaiXe, MaLichTrinh)
VALUES
    (6, '2026-10-15', '08:00:00', '11:00:00',
     150000.00, N'ChuaChay', 8, 6, 6, 5);

SET IDENTITY_INSERT dbo.CHUYEN_XE OFF;
GO

/* =========================
   3. KIEM TRA DU LIEU
   ========================= */

SELECT N'USER' AS TenBang, COUNT_BIG(*) AS SoBanGhi
FROM dbo.[USER]
UNION ALL
SELECT N'KHACH_HANG', COUNT_BIG(*) FROM dbo.KHACH_HANG
UNION ALL
SELECT N'NHA_XE', COUNT_BIG(*) FROM dbo.NHA_XE
UNION ALL
SELECT N'XE', COUNT_BIG(*) FROM dbo.XE
UNION ALL
SELECT N'TAI_XE', COUNT_BIG(*) FROM dbo.TAI_XE
UNION ALL
SELECT N'LICH_TRINH', COUNT_BIG(*) FROM dbo.LICH_TRINH
UNION ALL
SELECT N'CHUYEN_XE', COUNT_BIG(*) FROM dbo.CHUYEN_XE;
GO

SELECT
    cx.MaChuyen,
    lt.DiemDi,
    lt.DiemDen,
    cx.NgayKhoiHanh,
    cx.GioKhoiHanh,
    cx.GioDuKienDen,
    cx.GiaVe,
    nx.TenNhaXe,
    tx.HoTen AS HoTenTaiXe,
    xe.BienSo,
    cx.TrangThai
FROM dbo.CHUYEN_XE cx
JOIN dbo.LICH_TRINH lt ON cx.MaLichTrinh = lt.MaLichTrinh
JOIN dbo.NHA_XE nx ON cx.MaNhaXe = nx.MaUser
JOIN dbo.TAI_XE tx ON cx.MaTaiXe = tx.MaTaiXe
JOIN dbo.XE xe ON cx.MaXe = xe.MaXe;
GO
