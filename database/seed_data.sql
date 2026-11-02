-- ==============================================================
-- SEED DATA MẪU CHO CSDL MYSQL
-- ==============================================================

USE bus_management;

-- 1. USERS
INSERT INTO users (ma_user, username, password, email, so_dien_thoai, vai_tro, trang_thai) VALUES
(1, 'admin', 'admin123', 'admin@benxe.vn', '0243888999', 'ADMIN', 'HOAT_DONG'),
(2, 'hoanglong', '123456', 'hoanglong@mail.vn', '0900000001', 'NHA_XE', 'HOAT_DONG'),
(3, 'phuongtrang', '123456', 'pt@mail.vn', '0900000002', 'NHA_XE', 'HOAT_DONG'),
(4, 'khachhang', '123456', 'nguyenvana@gmail.com', '0987654321', 'KHACH_HANG', 'HOAT_DONG');

-- 2. NHA_XE
INSERT INTO nha_xe (ma_user, ten_nha_xe, dia_chi, so_dien_thoai, mo_ta, trang_thai) VALUES
(2, 'Nhà xe Hoàng Long', 'Bến xe Giáp Bát, Giải Phóng, Hà Nội', '0900000001', 'Dịch vụ xe khách giường nằm liên tỉnh chất lượng cao, chạy xuyên Bắc - Nam.', 'HOAT_DONG'),
(3, 'Nhà xe Phương Trang (FUTA)', 'Bến xe Miền Đông, TP. Hồ Chí Minh', '0900000002', 'Hệ thống xe khách Limousine và giường nằm phủ sóng toàn quốc.', 'HOAT_DONG');

-- 3. KHACH_HANG
INSERT INTO khach_hang (ma_user, ho_ten, dia_chi, ngay_sinh, cccd) VALUES
(4, 'Nguyễn Văn An', 'Hà Nội', '2000-05-20', '001200001234');

-- 4. XE
INSERT INTO xe (ma_xe, bien_so, loai_xe, so_cho, trang_thai, ma_nha_xe) VALUES
(1, '29B-12345', 'Giường nằm 40 chỗ', 40, 'CON_O_BEN', 2),
(2, '29B-22222', 'Limousine 22 chỗ', 22, 'XE_DI', 2),
(3, '51B-33333', 'Ghế ngồi 45 chỗ', 45, 'XE_DEN', 3),
(4, '51B-44444', 'Limousine 9 chỗ VIP', 9, 'CON_O_BEN', 3),
(5, '29B-88888', 'Giường nằm 34 chỗ', 34, 'BAO_TRI', 2);

-- 5. TAI_XE
INSERT INTO tai_xe (ma_tai_xe, ho_ten, so_dien_thoai, so_gplx, loai_bang, trang_thai, ma_nha_xe) VALUES
(1, 'Nguyễn Văn An', '0911111111', 'GPLX001', 'D', 'DANG_LAI', 2),
(2, 'Trần Minh Bình', '0922222222', 'GPLX002', 'E', 'RANH', 2),
(3, 'Lê Quốc Cường', '0933333333', 'GPLX003', 'D', 'NGHI', 3),
(4, 'Phạm Hoàng Nam', '0944444444', 'GPLX004', 'E', 'RANH', 3);

-- 6. TUYEN_XE
INSERT INTO tuyen_xe (ma_tuyen, ten_tuyen, diem_di, diem_den, thoi_gian_du_kien_phut, khoang_cach_km) VALUES
(1, 'Hà Nội - Hải Phòng', 'Hà Nội', 'Hải Phòng', 120, 120.0),
(2, 'Hà Nội - Sa Pa', 'Hà Nội', 'Sa Pa', 330, 320.0),
(3, 'Hà Nội - Đà Nẵng', 'Hà Nội', 'Đà Nẵng', 840, 760.0),
(4, 'TP. Hồ Chí Minh - Đà Lạt', 'TP. Hồ Chí Minh', 'Đà Lạt', 360, 305.0),
(5, 'TP. Hồ Chí Minh - Cần Thơ', 'TP. Hồ Chí Minh', 'Cần Thơ', 180, 165.0);

-- 7. CHUYEN_XE
INSERT INTO chuyen_xe (ma_chuyen, ma_nha_xe, ma_xe, ma_tai_xe, ma_tuyen, khoi_hanh, du_kien_den, gia_ve, trang_thai) VALUES
(1, 2, 2, 1, 1, NOW() + INTERVAL 1 HOUR, NOW() + INTERVAL 3 HOUR, 150000, 'DANG_CHAY'),
(2, 2, 1, 2, 2, NOW() + INTERVAL 5 HOUR, NOW() + INTERVAL 11 HOUR, 320000, 'SAP_KHOI_HANH'),
(3, 3, 4, 4, 4, NOW() + INTERVAL 1 DAY, NOW() + INTERVAL 1 DAY + INTERVAL 6 HOUR, 290000, 'SAP_KHOI_HANH'),
(4, 3, 3, 3, 5, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 3 HOUR, 180000, 'HOAN_THANH');

-- 8. VE_XE
INSERT INTO ve_xe (ma_ve, ma_ve_code, ma_chuyen, ma_khach_hang, ten_khach_hang, so_dien_thoai, email, so_ghe, gia_ve, ngay_dat, trang_thai) VALUES
(1, 'VE-1001-A01', 2, 4, 'Nguyễn Văn An', '0987654321', 'nguyenvana@gmail.com', 'A01', 320000, NOW() - INTERVAL 4 HOUR, 'DA_DAT'),
(2, 'VE-1001-A02', 2, 4, 'Trần Thị Mai', '0977112233', 'mai.tran@gmail.com', 'A02', 320000, NOW() - INTERVAL 3 HOUR, 'DA_DAT');

-- 9. DANH_GIA
INSERT INTO danh_gia (ma_danh_gia, ma_chuyen, ma_nha_xe, ma_khach_hang, ten_khach_hang, so_sao, noi_dung, ngay_danh_gia) VALUES
(1, 4, 3, 4, 'Nguyễn Văn An', 5, 'Xe chạy rất êm, tài xế lịch sự, bến xe hướng dẫn tận tình, 10 điểm!', NOW() - INTERVAL 1 DAY),
(2, 1, 2, 4, 'Trần Quốc Tuấn', 5, 'Chuyến xe khởi hành chuẩn giờ, xe sạch sẽ thơm tho, đặt vé online nhanh chóng.', NOW() - INTERVAL 2 HOUR);
