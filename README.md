# HỆ THỐNG QUẢN LÝ BẾN XE KHÁCH & ĐẶT VÉ TRỰC TUYẾN
> **Bài tập lớn môn Lập trình Hướng đối tượng (OOP) & Cơ sở dữ liệu**  
> **Công nghệ:** Java JDK 21 | Spring Boot 4.0.8 (Jakarta EE) | Port 8080 | In-Memory H2 & MySQL

---

## 1. Giới thiệu & Tổng quan dự án

Dự án xây dựng một ứng dụng web hoàn chỉnh theo dõi và tra cứu lịch trình xe, quản lý bến bãi và đặt vé trực tuyến:
1. **Phân quyền người dùng**:
   - **Ban Quản Lý Bến Xe (Admin)**: Giám sát toàn bộ điều độ xe ra/vào bến, thống kê lượng xe tại bến, quản lý danh sách và phê duyệt nhà xe.
   - **Doanh nghiệp Nhà xe**: Quản lý thông tin nhà xe, quản lý đội xe và trạng thái (xe đến, xe đi, còn ở bến, bảo dưỡng), quản lý tài xế theo chuyến, tạo và cập nhật chuyến xe, gán xe và tài xế, quản lý giá vé.
   - **Khách hàng**: Đăng ký/đăng nhập, cập nhật thông tin cá nhân, tra cứu và lọc chuyến xe theo chặng đường và thời gian, chọn ghế trực quan trên sơ đồ xe, đặt vé không trùng lặp, xuất phiếu vé điện tử có mã QR, đánh giá chuyến xe 1-5 sao kèm nhận xét.
2. **Tuân thủ thiết kế Hướng đối tượng (OOP)**:
   - Các thực thể cốt lõi đóng gói trong package `model/`: `User` (lớp cơ sở), `KhachHang extends User`, `NhaXe extends User`, `Xe`, `TaiXe`, `ChuyenXe`, `LichTrinh`, `DanhGia`.
   - Các dịch vụ xử lý nghiệp vụ trong package `service/`: `QuanLyXe`, `QuanLyChuyenXe`, `TimKiemChuyenXe`.
   - Điểm khởi chạy `Main.java` và `BusManagementApplication.java`.

---

## 2. Yêu cầu môi trường

- **Java Development Kit (JDK):** JDK 21 LTS (`java version "21.0.12.1"`).
- **Cổng máy chủ (Port):** `8080` ([http://localhost:8080/](http://localhost:8080/)).
- **Phiên bản Spring Boot:** `4.0.8` (Maven Central).
- **Cơ sở dữ liệu:**
  - **Mặc định:** CSDL In-Memory H2 – Chạy ngay lập tức không cần cài đặt phần mềm ngoài.
  - **Mở rộng MySQL:** Có sẵn `application-mysql.properties`, file `database/schema.sql` và `database/seed_data.sql` nếu cần kết nối MySQL Server.

---

## 3. Cấu trúc thư mục dự án

```text
BusManagement-System/
├── pom.xml                                   # Cấu hình Maven (Spring Boot 4.0.8, Java 21, JPA, H2, MySQL, Lombok)
├── README.md                                 # Tài liệu kiến trúc và hướng dẫn triển khai
│
├── database/                                 # Kịch bản cơ sở dữ liệu MySQL
│   ├── schema.sql                            # DDL tạo cấu trúc bảng CSDL
│   └── seed_data.sql                         # DML nạp dữ liệu mẫu
│
└── src/
    └── main/
        ├── java/com/ptit/busmanagement/
        │   ├── Main.java                     # Điểm khởi chạy chính thức theo chuẩn đề bài
        │   ├── BusManagementApplication.java # Spring Boot Application entrypoint
        │   │
        │   ├── model/                        # Các lớp đối tượng nghiệp vụ OOP chuẩn:
        │   │   ├── User.java                 # Lớp cơ sở cho thông tin tài khoản dùng chung
        │   │   ├── KhachHang.java            # Quản lý thông tin và nhu cầu tra cứu của khách (kế thừa User)
        │   │   ├── NhaXe.java                # Quản lý thông tin và hoạt động của nhà xe (kế thừa User)
        │   │   ├── Xe.java                   # Thông tin phương tiện, biển số, loại xe, số chỗ, trạng thái
        │   │   ├── TaiXe.java                # Thông tin tài xế gắn với chuyến xe, giấy phép lái xe
        │   │   ├── ChuyenXe.java             # Thực thể trung tâm (điểm đi/đến, thời gian, giá vé, xe, tài xế)
        │   │   ├── LichTrinh.java            # Quản lý thời gian di chuyển, lộ trình dừng đỗ
        │   │   └── DanhGia.java              # Đánh giá của khách hàng về chuyến xe
        │   │
        │   ├── service/                      # Tầng xử lý logic nghiệp vụ OOP:
        │   │   ├── QuanLyXe.java             # Thêm, sửa, xóa, tìm kiếm và cập nhật trạng thái xe bến bãi
        │   │   ├── QuanLyChuyenXe.java       # Tạo, cập nhật, xóa, phân công tài xế/xe cho chuyến
        │   │   ├── TimKiemChuyenXe.java      # Xử lý tiêu chí lọc điểm đi, điểm đến, thời gian, giá vé
        │   │   ├── AuthService.java          # Đăng ký, đăng nhập và phân quyền người dùng
        │   │   ├── VeXeService.java          # Đặt vé, giữ chỗ sơ đồ xe, chống trùng ghế, hủy vé
        │   │   └── DanhGiaService.java       # Quản lý nhận xét và xếp hạng sao
        │   │
        │   ├── controller/                   # Tầng REST API:
        │   │   ├── AuthController.java       # /api/auth/** (login, register)
        │   │   ├── KhachHangController.java  # /api/khach-hang/** (thông tin cá nhân khách)
        │   │   ├── NhaXeController.java      # /api/nha-xe/** (quản lý nhà xe)
        │   │   ├── XeController.java         # /api/xe/** (quản lý đội xe & thống kê bến bãi)
        │   │   ├── TaiXeController.java      # /api/tai-xe/** (hồ sơ tài xế)
        │   │   ├── ChuyenXeController.java   # /api/chuyen-xe/** (tìm kiếm, tạo, sửa chuyến xe)
        │   │   ├── LichTrinhController.java  # /api/lich-trinh/** (lịch trình di chuyển)
        │   │   ├── VeXeController.java       # /api/ve-xe/** (sơ đồ ghế & đặt vé trực quan)
        │   │   ├── DanhGiaController.java    # /api/danh-gia/** (đánh giá chuyến đi)
        │   │   └── ThongKeController.java    # /api/thong-ke/** (KPI bến bãi)
        │   │
        │   ├── entity/                       # Thực thể Jakarta Persistence (JPA) ánh xạ DB
        │   ├── repository/                   # Giao tiếp cơ sở dữ liệu (Spring Data JPA)
        │   ├── dto/                          # Data Transfer Objects (Request/Response)
        │   └── exception/                    # GlobalExceptionHandler chuẩn hóa mã lỗi HTTP
        │
        └── resources/
            ├── application.properties        # Cấu hình Spring Boot (Port 8080, H2 in-memory)
            ├── application-mysql.properties  # Cấu hình dự phòng cho MySQL
            └── static/                       # Giao diện Web Client hoàn chỉnh:
                ├── index.html                # Trang web chính (Tra cứu vé, Admin Bến, Nhà xe, Vé & Đánh giá)
                ├── css/styles.css            # Stylesheet giao diện hiện đại & sơ đồ ghế
                └── js/app.js                 # Xử lý tương tác AJAX REST API
```

---

## 4. Hướng dẫn khởi chạy ứng dụng

### Cách 1: Chạy file JAR đã đóng gói sẵn

```powershell
java -jar target/bus-management-system-1.0.0.jar
```

### Cách 2: Khởi chạy bằng lệnh Maven

```powershell
mvn spring-boot:run
```

Hoặc sử dụng Maven trong máy:
```powershell
& "D:\IntelliJ IDEA 2026.1.3\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run
```

### Cách 3: Mở bằng IntelliJ IDEA hoặc VS Code

1. Mở IDE (IntelliJ IDEA / VS Code) -> **Open Project** -> Chọn thư mục `BusManagement-System`.
2. Kiểm tra Project SDK: Chọn **Java 21**.
3. Chạy file `com.ptit.busmanagement.Main` hoặc `BusManagementApplication`.

Sau khi máy chủ khởi động:
- **Trang chủ Web Bến Xe:** [http://localhost:8080/](http://localhost:8080/)
- **H2 Web Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:busdb`, User: `sa`, Password: để trống).

---

## 5. Tài khoản dùng thử kiểm thử hệ thống

| Vai trò | Tên đăng nhập | Mật khẩu | Phạm vi quyền hạn |
| :--- | :--- | :--- | :--- |
| **Ban Quản Lý Bến (Admin)** | `admin` | `admin123` | Giám sát KPI bến bãi, quản lý nhà xe, điều độ xe đến/đi |
| **Nhà xe Hoàng Long** | `hoanglong` | `123456` | Quản lý phương tiện, tài xế, lập chuyến xe và theo dõi đặt vé |
| **Nhà xe Phương Trang (FUTA)** | `phuongtrang` | `123456` | Quản lý đội xe và chuyến xe khu vực phía Nam |
| **Hành khách** | `khachhang` | `123456` | Tìm chuyến, chọn ghế ngồi, đặt vé điện tử và gửi đánh giá |

*(Thanh điều hướng phía trên của giao diện Web có sẵn menu chuyển đổi vai trò nhanh để kiểm thử đầy đủ các chức năng).*

---

## 6. Danh mục REST API chính

| Nhóm chức năng | Phương thức | Đường dẫn API | Mô tả |
| :--- | :---: | :--- | :--- |
| **Xác thực** | `POST` | `/api/auth/login` | Đăng nhập tài khoản |
| | `POST` | `/api/auth/register` | Đăng ký tài khoản mới (Khách hàng hoặc Nhà xe) |
| **Khách hàng** | `GET` | `/api/khach-hang/{maUser}` | Xem thông tin cá nhân khách hàng |
| | `PUT` | `/api/khach-hang/{maUser}` | Cập nhật thông tin cá nhân khách hàng |
| **Nhà xe** | `GET` | `/api/nha-xe` | Danh sách nhà xe tại bến |
| | `POST` | `/api/nha-xe` | Đăng ký hồ sơ nhà xe mới |
| | `PATCH` | `/api/nha-xe/{id}/status` | Khóa/mở khóa tài khoản nhà xe |
| **Phương tiện & Bến bãi** | `GET` | `/api/xe` | Lọc danh sách xe theo nhà xe, trạng thái, biển số |
| | `GET` | `/api/xe/thong-ke` | Thống kê số lượng xe: đến bến, xuất bến, còn ở bến, bảo trì |
| | `POST` | `/api/xe` | Thêm xe mới vào đội xe |
| | `PATCH` | `/api/xe/{id}/trang-thai` | Cập nhật trạng thái điều độ bến bãi (`XE_DEN`, `XE_DI`, `CON_O_BEN`, `BAO_TRI`) |
| **Tài xế** | `GET` | `/api/tai-xe` | Danh sách tài xế theo nhà xe và trạng thái |
| | `POST` | `/api/tai-xe` | Thêm tài xế mới |
| **Chuyến xe** | `GET` | `/api/chuyen-xe` | Danh sách chuyến xe |
| | `GET` | `/api/chuyen-xe/search` | Tìm kiếm chuyến theo điểm đi, điểm đến, ngày khởi hành |
| | `POST` | `/api/chuyen-xe` | Lập chuyến xe mới (gán xe & tài xế) |
| | `PATCH` | `/api/chuyen-xe/{id}/trang-thai` | Đổi trạng thái: `SAP_KHOI_HANH`, `DANG_CHAY`, `HOAN_THANH`, `HUY` |
| **Lịch trình** | `GET` | `/api/lich-trinh` | Tra cứu lộ trình, khoảng cách và thời gian di chuyển |
| **Vé & Ghế ngồi** | `GET` | `/api/ve-xe/so-do-ghe/{id}` | Lấy sơ đồ ghế và danh sách ghế còn trống / đã đặt |
| | `POST` | `/api/ve-xe/dat-ve` | Đặt vé giữ chỗ theo sơ đồ ghế trực quan (chống trùng) |
| | `GET` | `/api/ve-xe/tra-cuu` | Tra cứu vé theo mã vé hoặc số điện thoại |
| | `PATCH` | `/api/ve-xe/{id}/huy` | Hủy vé và giải phóng ghế ngồi |
| **Đánh giá** | `POST` | `/api/danh-gia` | Khách hàng đánh giá chất lượng chuyến xe (1-5 sao) |
