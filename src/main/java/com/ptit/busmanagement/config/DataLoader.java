package com.ptit.busmanagement.config;

import com.ptit.busmanagement.entity.*;
import com.ptit.busmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Tự động nạp dữ liệu mẫu ban đầu để chạy thử nghiệm ngay lập tức.
 * Kế thừa và mở rộng trọn vẹn dữ liệu từ DuLieuMau của nhóm.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final NhaXeRepository nhaXeRepo;
    private final KhachHangRepository khachHangRepo;
    private final XeRepository xeRepo;
    private final TaiXeRepository taiXeRepo;
    private final TuyenXeRepository tuyenXeRepo;
    private final ChuyenXeRepository chuyenXeRepo;
    private final VeXeRepository veXeRepo;
    private final DanhGiaRepository danhGiaRepo;
    private final LichTrinhRepository lichTrinhRepo;

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;

        System.out.println(">>> Đang nạp dữ liệu mẫu khởi tạo hệ thống Quản Lý Bến Xe...");

        // 1. Tạo Users & Nhà Xe (Tương thích DuLieuMau)
        User uAdmin = User.builder()
                .username("admin")
                .password("admin123")
                .email("admin@benxe.vn")
                .soDienThoai("0243888999")
                .vaiTro("ADMIN")
                .trangThai("HOAT_DONG")
                .build();
        userRepo.save(uAdmin);

        User uHL = User.builder()
                .username("hoanglong")
                .password("123456")
                .email("hoanglong@mail.vn")
                .soDienThoai("0900000001")
                .vaiTro("NHA_XE")
                .trangThai("HOAT_DONG")
                .build();
        uHL = userRepo.save(uHL);

        NhaXe nxHL = NhaXe.builder()
                .maUser(uHL.getMaUser())
                .tenNhaXe("Nhà xe Hoàng Long")
                .diaChi("Bến xe Giáp Bát, Giải Phóng, Hà Nội")
                .soDienThoai("0900000001")
                .moTa("Dịch vụ xe khách giường nằm liên tỉnh chất lượng cao, chạy xuyên Bắc - Nam.")
                .trangThai("HOAT_DONG")
                .build();
        nhaXeRepo.save(nxHL);

        User uPT = User.builder()
                .username("phuongtrang")
                .password("123456")
                .email("pt@mail.vn")
                .soDienThoai("0900000002")
                .vaiTro("NHA_XE")
                .trangThai("HOAT_DONG")
                .build();
        uPT = userRepo.save(uPT);

        NhaXe nxPT = NhaXe.builder()
                .maUser(uPT.getMaUser())
                .tenNhaXe("Nhà xe Phương Trang (FUTA)")
                .diaChi("Bến xe Miền Đông, TP. Hồ Chí Minh")
                .soDienThoai("0900000002")
                .moTa("Hệ thống xe khách Limousine và giường nằm phủ sóng toàn quốc.")
                .trangThai("HOAT_DONG")
                .build();
        nhaXeRepo.save(nxPT);

        User uKH = User.builder()
                .username("khachhang")
                .password("123456")
                .email("nguyenvana@gmail.com")
                .soDienThoai("0987654321")
                .vaiTro("KHACH_HANG")
                .trangThai("HOAT_DONG")
                .build();
        uKH = userRepo.save(uKH);

        KhachHang kh = KhachHang.builder()
                .maUser(uKH.getMaUser())
                .hoTen("Nguyễn Văn An")
                .diaChi("Hà Nội")
                .ngaySinh(LocalDate.of(2000, 5, 20))
                .cccd("001200001234")
                .build();
        khachHangRepo.save(kh);

        // 2. Nạp Xe (Tương thích DuLieuMau)
        Xe xe1 = Xe.builder()
                .bienSo("29B-12345")
                .loaiXe("Giường nằm 40 chỗ")
                .soCho(40)
                .maNhaXe(nxHL.getMaUser())
                .trangThai(TrangThaiXe.CON_O_BEN)
                .build();
        xe1 = xeRepo.save(xe1);

        Xe xe2 = Xe.builder()
                .bienSo("29B-22222")
                .loaiXe("Limousine 22 chỗ")
                .soCho(22)
                .maNhaXe(nxHL.getMaUser())
                .trangThai(TrangThaiXe.XE_DI)
                .build();
        xe2 = xeRepo.save(xe2);

        Xe xe3 = Xe.builder()
                .bienSo("51B-33333")
                .loaiXe("Ghế ngồi 45 chỗ")
                .soCho(45)
                .maNhaXe(nxPT.getMaUser())
                .trangThai(TrangThaiXe.XE_DEN)
                .build();
        xe3 = xeRepo.save(xe3);

        Xe xe4 = Xe.builder()
                .bienSo("51B-44444")
                .loaiXe("Limousine 9 chỗ VIP")
                .soCho(9)
                .maNhaXe(nxPT.getMaUser())
                .trangThai(TrangThaiXe.CON_O_BEN)
                .build();
        xe4 = xeRepo.save(xe4);

        Xe xe5 = Xe.builder()
                .bienSo("29B-88888")
                .loaiXe("Giường nằm 34 chỗ")
                .soCho(34)
                .maNhaXe(nxHL.getMaUser())
                .trangThai(TrangThaiXe.BAO_TRI)
                .build();
        xeRepo.save(xe5);

        // 3. Nạp Tài Xế (Tương thích DuLieuMau)
        TaiXe tx1 = TaiXe.builder()
                .hoTen("Nguyễn Văn An")
                .soDienThoai("0911111111")
                .soGPLX("GPLX001")
                .loaiBang("D")
                .maNhaXe(nxHL.getMaUser())
                .trangThai(TrangThaiTaiXe.DANG_LAI)
                .build();
        tx1 = taiXeRepo.save(tx1);

        TaiXe tx2 = TaiXe.builder()
                .hoTen("Trần Minh Bình")
                .soDienThoai("0922222222")
                .soGPLX("GPLX002")
                .loaiBang("E")
                .maNhaXe(nxHL.getMaUser())
                .trangThai(TrangThaiTaiXe.RANH)
                .build();
        tx2 = taiXeRepo.save(tx2);

        TaiXe tx3 = TaiXe.builder()
                .hoTen("Lê Quốc Cường")
                .soDienThoai("0933333333")
                .soGPLX("GPLX003")
                .loaiBang("D")
                .maNhaXe(nxPT.getMaUser())
                .trangThai(TrangThaiTaiXe.NGHI)
                .build();
        tx3 = taiXeRepo.save(tx3);

        TaiXe tx4 = TaiXe.builder()
                .hoTen("Phạm Hoàng Nam")
                .soDienThoai("0944444444")
                .soGPLX("GPLX004")
                .loaiBang("E")
                .maNhaXe(nxPT.getMaUser())
                .trangThai(TrangThaiTaiXe.RANH)
                .build();
        tx4 = taiXeRepo.save(tx4);

        // 4. Nạp Tuyến Xe (Lịch trình)
        TuyenXe t1 = TuyenXe.builder()
                .tenTuyen("Hà Nội - Hải Phòng")
                .diemDi("Hà Nội")
                .diemDen("Hải Phòng")
                .thoiGianDuKienPhut(120)
                .khoangCachKm(120.0)
                .build();
        t1 = tuyenXeRepo.save(t1);

        TuyenXe t2 = TuyenXe.builder()
                .tenTuyen("Hà Nội - Sa Pa")
                .diemDi("Hà Nội")
                .diemDen("Sa Pa")
                .thoiGianDuKienPhut(330)
                .khoangCachKm(320.0)
                .build();
        t2 = tuyenXeRepo.save(t2);

        TuyenXe t3 = TuyenXe.builder()
                .tenTuyen("Hà Nội - Đà Nẵng")
                .diemDi("Hà Nội")
                .diemDen("Đà Nẵng")
                .thoiGianDuKienPhut(840)
                .khoangCachKm(760.0)
                .build();
        t3 = tuyenXeRepo.save(t3);

        TuyenXe t4 = TuyenXe.builder()
                .tenTuyen("TP. Hồ Chí Minh - Đà Lạt")
                .diemDi("TP. Hồ Chí Minh")
                .diemDen("Đà Lạt")
                .thoiGianDuKienPhut(360)
                .khoangCachKm(305.0)
                .build();
        t4 = tuyenXeRepo.save(t4);

        TuyenXe t5 = TuyenXe.builder()
                .tenTuyen("TP. Hồ Chí Minh - Cần Thơ")
                .diemDi("TP. Hồ Chí Minh")
                .diemDen("Cần Thơ")
                .thoiGianDuKienPhut(180)
                .khoangCachKm(165.0)
                .build();
        t5 = tuyenXeRepo.save(t5);

        // 4.1. Nạp Lịch Trình (LichTrinh OOP)
        lichTrinhRepo.save(LichTrinh.builder()
                .diemDi("Hà Nội")
                .diemDen("Hải Phòng")
                .thoiGianDuKienPhut(120)
                .khoangCachKm(120.0)
                .loTrinhChiTiet("Bến xe Mỹ Đình -> Cao tốc Hà Nội - Hải Phòng -> Bến xe Cầu Rào")
                .thoiGianDungNghiPhut(15)
                .build());

        lichTrinhRepo.save(LichTrinh.builder()
                .diemDi("Hà Nội")
                .diemDen("Sa Pa")
                .thoiGianDuKienPhut(330)
                .khoangCachKm(320.0)
                .loTrinhChiTiet("Bến xe Mỹ Đình -> Cao tốc Nội Bài - Lào Cai -> Sa Pa")
                .thoiGianDungNghiPhut(30)
                .build());

        lichTrinhRepo.save(LichTrinh.builder()
                .diemDi("TP. Hồ Chí Minh")
                .diemDen("Đà Lạt")
                .thoiGianDuKienPhut(360)
                .khoangCachKm(305.0)
                .loTrinhChiTiet("Bến xe Miền Đông -> Cao tốc Long Thành - Dầu Giây -> Quốc lộ 20 -> Bến xe Liên tỉnh Đà Lạt")
                .thoiGianDungNghiPhut(30)
                .build());

        // 5. Nạp Chuyến Xe Mẫu
        LocalDateTime today = LocalDateTime.now();

        ChuyenXe cx1 = ChuyenXe.builder()
                .maNhaXe(nxHL.getMaUser())
                .maXe(xe2.getMaXe())
                .maTaiXe(tx1.getMaTaiXe())
                .maTuyen(t1.getMaTuyen())
                .khoiHanh(today.withHour(8).withMinute(0).withSecond(0))
                .duKienDen(today.withHour(10).withMinute(0).withSecond(0))
                .giaVe(150000L)
                .trangThai(TrangThaiChuyenXe.DANG_CHAY)
                .build();
        cx1 = chuyenXeRepo.save(cx1);

        ChuyenXe cx2 = ChuyenXe.builder()
                .maNhaXe(nxHL.getMaUser())
                .maXe(xe1.getMaXe())
                .maTaiXe(tx2.getMaTaiXe())
                .maTuyen(t2.getMaTuyen())
                .khoiHanh(today.withHour(14).withMinute(30).withSecond(0))
                .duKienDen(today.withHour(20).withMinute(0).withSecond(0))
                .giaVe(320000L)
                .trangThai(TrangThaiChuyenXe.SAP_KHOI_HANH)
                .build();
        cx2 = chuyenXeRepo.save(cx2);

        ChuyenXe cx3 = ChuyenXe.builder()
                .maNhaXe(nxPT.getMaUser())
                .maXe(xe4.getMaXe())
                .maTaiXe(tx4.getMaTaiXe())
                .maTuyen(t4.getMaTuyen())
                .khoiHanh(today.plusDays(1).withHour(7).withMinute(0).withSecond(0))
                .duKienDen(today.plusDays(1).withHour(13).withMinute(0).withSecond(0))
                .giaVe(290000L)
                .trangThai(TrangThaiChuyenXe.SAP_KHOI_HANH)
                .build();
        cx3 = chuyenXeRepo.save(cx3);

        ChuyenXe cx4 = ChuyenXe.builder()
                .maNhaXe(nxPT.getMaUser())
                .maXe(xe3.getMaXe())
                .maTaiXe(tx3.getMaTaiXe())
                .maTuyen(t5.getMaTuyen())
                .khoiHanh(today.minusDays(1).withHour(9).withMinute(0).withSecond(0))
                .duKienDen(today.minusDays(1).withHour(12).withMinute(0).withSecond(0))
                .giaVe(180000L)
                .trangThai(TrangThaiChuyenXe.HOAN_THANH)
                .build();
        cx4 = chuyenXeRepo.save(cx4);

        // 6. Nạp Vé Xe Mẫu
        VeXe v1 = VeXe.builder()
                .maVeCode("VE-1001-A01")
                .maChuyen(cx2.getMaChuyen())
                .maKhachHang(uKH.getMaUser())
                .tenKhachHang("Nguyễn Văn An")
                .soDienThoai("0987654321")
                .email("nguyenvana@gmail.com")
                .soGhe("A01")
                .giaVe(320000L)
                .ngayDat(today.minusHours(4))
                .trangThai(TrangThaiVe.DA_DAT)
                .build();
        veXeRepo.save(v1);

        VeXe v2 = VeXe.builder()
                .maVeCode("VE-1001-A02")
                .maChuyen(cx2.getMaChuyen())
                .maKhachHang(uKH.getMaUser())
                .tenKhachHang("Trần Thị Mai")
                .soDienThoai("0977112233")
                .email("mai.tran@gmail.com")
                .soGhe("A02")
                .giaVe(320000L)
                .ngayDat(today.minusHours(3))
                .trangThai(TrangThaiVe.DA_DAT)
                .build();
        veXeRepo.save(v2);

        VeXe v3 = VeXe.builder()
                .maVeCode("VE-1002-A01")
                .maChuyen(cx3.getMaChuyen())
                .maKhachHang(uKH.getMaUser())
                .tenKhachHang("Lê Minh Đức")
                .soDienThoai("0912345678")
                .email("duc.lm@gmail.com")
                .soGhe("A01")
                .giaVe(290000L)
                .ngayDat(today.minusHours(1))
                .trangThai(TrangThaiVe.DA_DAT)
                .build();
        veXeRepo.save(v3);

        // 7. Nạp Đánh Giá Mẫu
        DanhGia dg1 = DanhGia.builder()
                .maChuyen(cx4.getMaChuyen())
                .maNhaXe(nxPT.getMaUser())
                .maKhachHang(uKH.getMaUser())
                .tenKhachHang("Nguyễn Văn An")
                .soSao(5)
                .noiDung("Xe chạy rất êm, tài xế lịch sự, bến xe hướng dẫn tận tình, 10 điểm!")
                .ngayDanhGia(today.minusDays(1).plusHours(5))
                .build();
        danhGiaRepo.save(dg1);

        DanhGia dg2 = DanhGia.builder()
                .maChuyen(cx1.getMaChuyen())
                .maNhaXe(nxHL.getMaUser())
                .maKhachHang(uKH.getMaUser())
                .tenKhachHang("Trần Quốc Tuấn")
                .soSao(5)
                .noiDung("Chuyến xe khởi hành chuẩn giờ, xe sạch sẽ thơm tho, đặt vé online nhanh chóng.")
                .ngayDanhGia(today.minusHours(2))
                .build();
        danhGiaRepo.save(dg2);

        System.out.println(">>> Nạp dữ liệu mẫu hoàn tất! Hệ thống đã sẵn sàng phục vụ tại port 8080.");
    }
}
