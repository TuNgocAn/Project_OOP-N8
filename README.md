# BUSTRACK - HỆ THỐNG THEO DÕI GIỜ XE & ĐẶT VÉ VỀ QUÊ
> **Bài tập lớn môn: Lập trình Hướng đối tượng (BTL OOP)**  
> **Nền tảng**: Java 17+ (Java 21/26), Java Swing + FlatLaf UI, Google Gson, JUnit 5, Apache Maven.

---

## 📖 1. Giới thiệu dự án

Ứng dụng **BusTrack** giải quyết nhu cầu thực tế của người dân, học sinh, sinh viên và người đi làm khi có nhu cầu **tra cứu giờ xe về quê, theo dõi lịch trình, giá vé và đặt vé**. Đồng thời, hệ thống cung cấp công cụ quản trị chuyên nghiệp cho **Nhà xe** để **quản lý đội xe, theo dõi xe đến, xe đi, số lượng xe còn ở bến, gán thông tin tài xế và thời gian di chuyển của từng xe**.

---

## 🎯 2. Tính năng chính theo 2 nhóm đối tượng người dùng

### 🚌 Phân hệ 1: Dành cho Nhà xe (Bus Operator)
1. **Quản lý bến bãi & Theo dõi trạng thái xe**:
   - Theo dõi thời gian thực: **Số lượng xe còn ở bến** (xe đến), **Số lượng xe đang di chuyển trên đường** (xe đi), **Xe đang bảo dưỡng**.
   - Cập nhật linh hoạt trạng thái xe: *Ở bến (AT_STATION)* $\leftrightarrow$ *Đang di chuyển (IN_TRANSIT)* $\leftrightarrow$ *Bảo dưỡng (MAINTENANCE)*.
2. **Quản lý phương tiện (Đội xe)**:
   - Thêm, sửa, xóa xe. Hỗ trợ đa dạng các loại xe: **Xe ghế ngồi**, **Xe giường nằm (2 tầng)**, **Xe Limousine VIP**.
3. **Quản lý Chuyến xe & Lịch trình**:
   - Thêm chuyến xe về quê: Điểm đón (Bến đi), Điểm trả (Chặng về quê), Giờ xuất bến, Thời gian di chuyển dự kiến, Giờ đến nơi, Giá vé.
   - **Quản lý tài xế cho chuyến xe**: Thêm và cập nhật thông tin tài xế cho mỗi chuyến xe (Họ tên, SĐT, Số giấy phép lái xe, kinh nghiệm).
   - Điều phối trạng thái chuyến: *Sắp khởi hành* $\rightarrow$ *Xuất bến (chạy)* $\rightarrow$ *Đến bến (hoàn thành)* $\rightarrow$ *Hủy chuyến*. Tự động đồng bộ trạng thái xe tương ứng.
4. **Xem Đánh giá từ khách hàng**:
   - Xem phản hồi, số sao chấm điểm (1 - 5 ⭐) và nhận xét của hành khách để cải thiện chất lượng dịch vụ.

### 👤 Phân hệ 2: Dành cho Khách hàng (Customer / Passenger)
1. **Quản lý thông tin cá nhân**:
   - Hồ sơ người dùng: Họ tên, Số điện thoại, Email, Quê quán (nơi thường về quê).
2. **Tra cứu chuyến xe về quê**:
   - Tìm kiếm chuyến xe linh hoạt: Theo Nơi đi, Chặng về quê (Điểm đến), Ngày đi.
   - Nút điền nhanh quê quán: Tự động điền quê của bạn chỉ với 1 click.
   - Hiển thị đầy đủ thông tin: Giờ xe xuất bến, thời gian di chuyển, giờ đến nơi dự kiến, loại xe, biển số, **thông tin tài xế liên hệ (Tên & SĐT)**, giá vé và số ghế còn trống.
3. **Chính sách giá vé linh hoạt (Strategy Pattern)**:
   - Giá ngày thường tiêu chuẩn.
   - Ưu đãi sinh viên về quê (Giảm 10%).
   - Giá cao điểm Lễ / Tết (Phụ thu 20%).
4. **Đặt chỗ & Quản lý vé ("Chuyến xe của tôi")**:
   - Đặt vé cho chuyến đi, chọn số lượng ghế, nhập điểm đón thuận tiện.
   - Theo dõi lịch trình xe chạy, liên hệ tài xế.
   - Hủy vé trước giờ chạy (tự động hoàn lại chỗ trống cho chuyến xe).
5. **Đánh giá & Góp ý chất lượng**:
   - Đánh giá sao (1-5 sao) và để lại bình luận nhận xét về chuyến xe/nhà xe.

---

## 🏛️ 3. Thiết kế Hướng Đối Tượng (OOP Architecture)

Dự án áp dụng chặt chẽ **4 Tính chất cốt lõi của OOP** và các **Mẫu thiết kế (Design Patterns)** kinh điển:

```
                               ┌─────────────┐
                               │    User     │ (Abstract)
                               └──────┬──────┘
                                      │
                   ┌──────────────────┴──────────────────┐
                   ▼                                     ▼
            ┌──────────────┐                      ┌─────────────┐
            │   Customer   │                      │ BusOperator │
            └──────────────┘                      └─────────────┘

                               ┌─────────────┐
                               │   Vehicle   │ (Abstract)
                               └──────┬──────┘
                                      │
             ┌────────────────────────┼────────────────────────┐
             ▼                        ▼                        ▼
      ┌─────────────┐          ┌──────────────┐         ┌──────────────┐
      │  SeaterBus  │          │  SleeperBus  │         │ LimousineBus │
      └─────────────┘          └──────────────┘         └──────────────┘
```

### 1. Đóng gói (Encapsulation)
- Toàn bộ các thuộc tính trong model (`User`, `Vehicle`, `Trip`, `Driver`, `Booking`, `Review`) đều được khai báo `private`.
- Dữ liệu được truy cập và chỉnh sửa an toàn thông qua Getter/Setter kèm kiểm tra ràng buộc (Validation): `ValidationUtils.isValidPhoneNumber()`, `ValidationUtils.isValidLicensePlate()`, kiểm tra giá vé $> 0$, thời gian hợp lệ.

### 2. Kế thừa (Inheritance)
- `User` $\rightarrow$ `Customer` và `BusOperator`.
- `Vehicle` $\rightarrow$ `SeaterBus`, `SleeperBus`, `LimousineBus`. Tái sử dụng tối đa mã nguồn chung về biển số, số chỗ, trạng thái, bến đỗ.

### 3. Đa hình (Polymorphism)
- Phương thức `getAmenityDescription()` và `getPriceMultiplier()` được ghi đè (`@Override`) cụ thể ở từng lớp xe con (`SeaterBus`, `SleeperBus`, `LimousineBus`).
- Giao diện `IPricingStrategy` được triển khai đa hình qua các lớp chiến lược giá: `StandardPricingStrategy`, `StudentDiscountPricingStrategy`, `HolidayPricingStrategy`.
- `IRepository<T, ID>` được cài đặt tổng quát qua `AbstractJsonRepository<T>`.

### 4. Trừu tượng hóa (Abstraction)
- Sử dụng các lớp trừu tượng `User`, `Vehicle`, `AbstractJsonRepository`.
- Phân tầng ứng dụng rõ ràng qua Interfaces: `IAuthService`, `IVehicleService`, `ITripService`, `IBookingService`, `IReviewService`.

### 5. Design Patterns áp dụng
- **Singleton Pattern**: `SessionManager` (quản lý phiên đăng nhập duy nhất) và `AppContext` (quản lý container dịch vụ).
- **Factory Method Pattern**: `VehicleFactory` (khởi tạo đối tượng xe), `UserFactory` (khởi tạo người dùng).
- **Strategy Pattern**: `IPricingStrategy` (tính giá vé tùy biến theo đối tượng và thời điểm).
- **Repository Pattern**: Tách biệt hoàn toàn tầng lưu trữ dữ liệu JSON với tầng xử lý nghiệp vụ Service.

---

## 📂 4. Cấu trúc thư mục mã nguồn

```
d:/BTL OOP/
├── pom.xml                                   # Cấu hình Maven
├── README.md                                 # Tài liệu dự án
├── data/                                     # Dữ liệu JSON (tự động tạo & lưu trữ)
│   ├── users.json
│   ├── vehicles.json
│   ├── trips.json
│   ├── bookings.json
│   └── reviews.json
│
└── src/
    ├── main/java/com/bustrack/
    │   ├── Main.java                         # Điểm khởi chạy ứng dụng
    │   │
    │   ├── model/                            # Tầng Mô hình Thực thể (OOP Models)
    │   │   ├── user/ (User, Customer, BusOperator, Role)
    │   │   ├── vehicle/ (Vehicle, SeaterBus, SleeperBus, LimousineBus, VehicleType, VehicleStatus)
    │   │   ├── trip/ (Trip, Driver, TripStatus)
    │   │   ├── booking/ (Booking, BookingStatus)
    │   │   └── review/ (Review)
    │   │
    │   ├── repository/                       # Tầng Lưu trữ dữ liệu (Repository Pattern)
    │   │   ├── IRepository.java
    │   │   ├── AbstractJsonRepository.java
    │   │   ├── UserRepository.java
    │   │   ├── VehicleRepository.java
    │   │   ├── TripRepository.java
    │   │   ├── BookingRepository.java
    │   │   └── ReviewRepository.java
    │   │
    │   ├── service/                          # Tầng Xử lý nghiệp vụ (Business Services)
    │   │   ├── IAuthService.java & AuthService.java
    │   │   ├── IVehicleService.java & VehicleService.java
    │   │   ├── ITripService.java & TripService.java
    │   │   ├── IBookingService.java & BookingService.java
    │   │   └── IReviewService.java & ReviewService.java
    │   │
    │   ├── strategy/                         # Mẫu Strategy Pattern
    │   │   ├── IPricingStrategy.java
    │   │   ├── StandardPricingStrategy.java
    │   │   ├── StudentDiscountPricingStrategy.java
    │   │   └── HolidayPricingStrategy.java
    │   │
    │   ├── factory/                          # Mẫu Factory Pattern
    │   │   ├── VehicleFactory.java
    │   │   └── UserFactory.java
    │   │
    │   ├── util/                             # Tiện ích hệ thống
    │   │   ├── AppContext.java               # Service Locator / Singleton
    │   │   ├── SessionManager.java           # Quản lý phiên làm việc
    │   │   ├── GsonUtils.java                # Cấu hình JSON Adapter đa hình
    │   │   ├── DateTimeUtils.java            # Xử lý ngày giờ
    │   │   ├── ValidationUtils.java          # Kiểm tra dữ liệu hợp lệ
    │   │   └── MockDataInitializer.java      # Tự sinh dữ liệu mẫu phong phú
    │   │
    │   └── ui/                               # Giao diện đồ họa (Java Swing + FlatLaf)
    │       ├── UITheme.java                  # Cấu hình màu sắc, theme phẳng hiện đại
    │       ├── LoginFrame.java               # Màn hình Đăng nhập (kèm nút Demo nhanh)
    │       ├── RegisterDialog.java           # Hộp thoại Đăng ký tài khoản
    │       ├── operator/
    │       │   └── OperatorMainFrame.java    # Giao diện dành riêng cho Nhà xe
    │       └── customer/
    │           └── CustomerMainFrame.java    # Giao diện dành riêng cho Khách hàng
    │
    └── test/java/com/bustrack/               # Unit Test (JUnit 5)
        ├── PricingStrategyTest.java
        ├── TripServiceTest.java
        └── VehicleServiceTest.java
```

---

## 🚀 5. Hướng dẫn biên dịch và chạy ứng dụng

### Cách 1: Chạy bằng dòng lệnh với Maven (Nhanh nhất)
Mở PowerShell hoặc Command Prompt tại thư mục dự án `d:\BTL OOP`:

1. **Biên dịch và chạy Unit Tests**:
   ```powershell
   & "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" clean test
   ```
2. **Khởi chạy ứng dụng**:
   ```powershell
   & "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java
   ```

### Cách 2: Chạy trong IDE (IntelliJ IDEA / Eclipse / NetBeans / VSCode)
1. Mở IDE của bạn lên và chọn **Open Project** $\rightarrow$ chọn thư mục `d:\BTL OOP`.
2. IDE sẽ tự động nhận diện file `pom.xml` và tải các thư viện (FlatLaf, Gson, JUnit).
3. Mở file `src/main/java/com/bustrack/Main.java` và bấm **Run** (hoặc `Shift + F10`).

---

## 🔑 6. Tài khoản mẫu có sẵn để Demo & Chấm điểm

Hệ thống đã tự động tích hợp sẵn các tài khoản mẫu với dữ liệu chuyến xe thực tế, ngay trên màn hình đăng nhập có sẵn các nút bấm **Đăng nhập nhanh 1-Click**:

| Vai trò | Tên đăng nhập | Mật khẩu | Mô tả |
| :--- | :--- | :--- | :--- |
| **Khách hàng** | `khachhang` | `123456` | Khách hàng Nguyễn Văn An (Quê Nam Định) |
| **Khách hàng** | `minhhoang` | `123456` | Khách hàng Lê Minh Hoàng (Quê Nghệ An) |
| **Nhà xe** | `hoanglong` | `123456` | Nhà xe Hoàng Long (Tuyến Hà Nội - Nam Định, Thanh Hóa, Nghệ An) |
| **Nhà xe** | `phuongtrang` | `123456` | Nhà xe Phương Trang (Tuyến TP.HCM - Đà Lạt, Cần Thơ) |
