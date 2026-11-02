/**
 * ==============================================================
 * HỆ THỐNG QUẢN LÝ BẾN XE KHÁCH - FRONTEND APP JAVASCRIPT
 * ==============================================================
 */

// Global State
const state = {
    currentRole: 'ADMIN', // ADMIN, NHA_XE, KHACH_HANG
    currentUserId: 1,
    currentNhaXeId: 1, // ID của Nhà xe đang thao tác
    selectedTrip: null,
    selectedSeats: [],
    trips: [],
    vehicles: [],
    drivers: [],
    nhaxes: [],
    routes: []
};

// ==============================================================
// 1. UTILITY FUNCTIONS & TOASTS
// ==============================================================

function formatVND(amount) {
    if (!amount) return '0 ₫';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function formatDateTime(dtStr) {
    if (!dtStr) return 'N/A';
    const d = new Date(dtStr);
    if (isNaN(d)) return dtStr;
    const hours = String(d.getHours()).padStart(2, '0');
    const mins = String(d.getMinutes()).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${hours}:${mins} - ${day}/${month}/${year}`;
}

function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `custom-toast toast-${type}`;
    
    let icon = 'fa-circle-check';
    if (type === 'error') icon = 'fa-circle-xmark';
    if (type === 'info') icon = 'fa-circle-info';

    toast.innerHTML = `<i class="fa-solid ${icon} fs-5"></i> <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// Switch User Role (Demo Convenience)
function switchUserRole(role, id, label) {
    state.currentRole = role;
    state.currentUserId = id;
    if (role === 'NHA_XE') state.currentNhaXeId = id;
    document.getElementById('current-user-display').textContent = label;
    showToast(`Đã chuyển đổi sang góc nhìn: ${label}`, 'info');

    if (role === 'ADMIN') {
        const tabBtn = document.getElementById('tab-admin-btn');
        if (tabBtn) tabBtn.click();
        loadAdminDashboard();
    } else if (role === 'NHA_XE') {
        const tabBtn = document.getElementById('tab-nhaxe-btn');
        if (tabBtn) tabBtn.click();
        loadOperatorPortal();
    } else {
        const tabBtn = document.getElementById('tab-home-btn');
        if (tabBtn) tabBtn.click();
    }
}

// ==============================================================
// 2. TAB 1: KHÁCH HÀNG - TÌM KIẾM & ĐẶT VÉ
// ==============================================================

async function loadAllTrips() {
    try {
        const res = await fetch('/api/chuyen-xe');
        const data = await res.json();
        state.trips = data;
        renderTripsList(data);
        populateReviewTripsSelect(data);
    } catch (err) {
        console.error('Lỗi tải danh sách chuyến xe:', err);
    }
}

async function handleSearchTrips(e) {
    if (e) e.preventDefault();
    const diemDi = document.getElementById('search-diem-di').value;
    const diemDen = document.getElementById('search-diem-den').value;
    const ngayDi = document.getElementById('search-ngay-di').value;

    const params = new URLSearchParams();
    if (diemDi) params.append('diemDi', diemDi);
    if (diemDen) params.append('diemDen', diemDen);
    if (ngayDi) params.append('ngayDi', ngayDi);

    try {
        const res = await fetch(`/api/chuyen-xe/search?${params.toString()}`);
        const data = await res.json();
        renderTripsList(data);
        showToast(`Tìm thấy ${data.length} chuyến xe phù hợp`, 'info');
    } catch (err) {
        showToast('Lỗi khi tra cứu chuyến xe', 'error');
    }
}

function renderTripsList(trips) {
    const container = document.getElementById('trips-container');
    document.getElementById('total-trips-count').textContent = `${trips.length} chuyến`;

    if (trips.length === 0) {
        container.innerHTML = `
            <div class="col-12 text-center py-5">
                <i class="fa-solid fa-bus-simple fa-3x text-muted mb-3 opacity-50"></i>
                <h5 class="text-muted">Không tìm thấy chuyến xe nào phù hợp với điều kiện lọc.</h5>
                <p class="small text-muted">Vui lòng thử lại với ngày khởi hành hoặc địa điểm khác.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = trips.map(t => {
        let statusBadge = `<span class="badge bg-success">Sắp khởi hành</span>`;
        if (t.trangThai === 'DANG_CHAY') statusBadge = `<span class="badge bg-primary">Đang di chuyển</span>`;
        if (t.trangThai === 'HOAN_THANH') statusBadge = `<span class="badge bg-secondary">Đã hoàn thành</span>`;
        if (t.trangThai === 'DA_HUY') statusBadge = `<span class="badge bg-danger">Đã hủy chuyến</span>`;

        const canBook = t.trangThai === 'SAP_KHOI_HANH' && (t.soGheTrong > 0);

        return `
            <div class="col-lg-6">
                <div class="custom-card p-4 h-100 d-flex flex-column justify-content-between position-relative">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <div>
                            <span class="badge bg-primary bg-opacity-10 text-primary fw-bold px-2 py-1 mb-1">
                                <i class="fa-solid fa-building me-1"></i> ${t.tenNhaXe || 'Nhà xe đối tác'}
                            </span>
                            <h5 class="fw-bold m-0 text-dark">${t.tenTuyen || 'Tuyến liên tỉnh'}</h5>
                        </div>
                        <div class="text-end">
                            <div class="fs-4 fw-bold text-danger">${formatVND(t.giaVe)}</div>
                            <div class="small text-muted">/ vé / ghế</div>
                        </div>
                    </div>

                    <div class="my-3 p-3 bg-light rounded-3">
                        <div class="row g-2 align-items-center">
                            <div class="col-5">
                                <div class="small text-muted">Xuất bến:</div>
                                <div class="fw-bold fs-6 text-dark">${formatDateTime(t.khoiHanh)}</div>
                                <div class="small text-primary"><i class="fa-solid fa-location-dot me-1"></i>${t.diemDi || 'Bến xe'}</div>
                            </div>
                            <div class="col-2 text-center text-muted">
                                <i class="fa-solid fa-arrow-right-long fs-5"></i>
                            </div>
                            <div class="col-5 text-end">
                                <div class="small text-muted">Dự kiến đến:</div>
                                <div class="fw-bold fs-6 text-dark">${formatDateTime(t.duKienDen)}</div>
                                <div class="small text-danger"><i class="fa-solid fa-location-crosshairs me-1"></i>${t.diemDen || 'Đích đến'}</div>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center pt-2 border-top">
                        <div class="small">
                            <span class="text-muted"><i class="fa-solid fa-van-shuttle me-1"></i>${t.bienSoXe || 'Xe'} (${t.loaiXe || 'Tiêu chuẩn'})</span>
                            <br>
                            <span class="fw-semibold text-success"><i class="fa-solid fa-chair me-1"></i>Còn ${t.soGheTrong ?? t.soCho ?? 0} ghế trống</span>
                        </div>
                        <div>
                            ${canBook ? `
                                <button class="btn btn-primary fw-bold px-3 py-2" onclick="openSeatMapModal(${t.maChuyen})">
                                    <i class="fa-solid fa-ticket me-1"></i> Chọn Ghế &amp; Đặt Vé
                                </button>
                            ` : `
                                <button class="btn btn-secondary disabled fw-bold px-3 py-2">
                                    Hết Chỗ / Đã Chạy
                                </button>
                            `}
                        </div>
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

// Sơ Đồ Chọn Ghế (Interactive Seat Map)
async function openSeatMapModal(tripId) {
    try {
        const res = await fetch(`/api/ve-xe/so-do-ghe/${tripId}`);
        const data = await res.json();
        state.selectedTrip = data;
        state.selectedSeats = [];

        document.getElementById('seatmap-trip-title').textContent = `Chuyến xe #${data.maChuyen}`;
        document.getElementById('seatmap-bus-info').textContent = `Xe: ${data.bienSo} | ${data.loaiXe} (${data.soCho} chỗ)`;
        document.getElementById('ticket-unit-price').textContent = formatVND(data.giaVe);
        updateSelectedSeatsDisplay();

        // Render ghế dựa theo số chỗ của xe
        const grid = document.getElementById('bus-seat-grid');
        grid.innerHTML = '';
        const totalSeats = data.soCho || 40;
        const booked = data.danhSachGheDaDat || [];

        for (let i = 1; i <= totalSeats; i++) {
            const seatCode = (i <= 20 ? 'A' : 'B') + String((i - 1) % 20 + 1).padStart(2, '0');
            const isBooked = booked.includes(seatCode);

            const seatEl = document.createElement('div');
            seatEl.className = `bus-seat ${isBooked ? 'booked' : 'available'}`;
            seatEl.id = `seat-${seatCode}`;
            seatEl.innerHTML = `<i class="fa-solid fa-couch"></i><span>${seatCode}</span>`;

            if (!isBooked) {
                seatEl.onclick = () => toggleSeatSelection(seatCode);
            } else {
                seatEl.title = `Ghế ${seatCode} đã có khách mua`;
            }

            grid.appendChild(seatEl);
        }

        const modal = new bootstrap.Modal(document.getElementById('seatMapModal'));
        modal.show();
    } catch (err) {
        showToast('Không thể mở sơ đồ ghế: ' + err.message, 'error');
    }
}

function toggleSeatSelection(seatCode) {
    const seatEl = document.getElementById(`seat-${seatCode}`);
    if (!seatEl) return;

    if (state.selectedSeats.includes(seatCode)) {
        state.selectedSeats = state.selectedSeats.filter(s => s !== seatCode);
        seatEl.classList.remove('selected');
        seatEl.classList.add('available');
    } else {
        state.selectedSeats.push(seatCode);
        seatEl.classList.remove('available');
        seatEl.classList.add('selected');
    }
    updateSelectedSeatsDisplay();
}

function updateSelectedSeatsDisplay() {
    const textEl = document.getElementById('selected-seats-text');
    const totalEl = document.getElementById('ticket-total-price');

    if (state.selectedSeats.length === 0) {
        textEl.textContent = 'Chưa chọn ghế';
        totalEl.textContent = '0 ₫';
    } else {
        textEl.textContent = state.selectedSeats.join(', ');
        const total = state.selectedSeats.length * (state.selectedTrip?.giaVe || 0);
        totalEl.textContent = formatVND(total);
    }
}

async function handleConfirmBooking(e) {
    e.preventDefault();
    if (state.selectedSeats.length === 0) {
        showToast('Vui lòng chọn ít nhất 1 ghế trống trên sơ đồ xe!', 'error');
        return;
    }

    const name = document.getElementById('book-passenger-name').value;
    const phone = document.getElementById('book-passenger-phone').value;
    const email = document.getElementById('book-passenger-email').value;

    const payload = {
        maChuyen: state.selectedTrip.maChuyen,
        maKhachHang: state.currentUserId,
        tenKhachHang: name,
        soDienThoai: phone,
        email: email,
        soGhe: state.selectedSeats.join(', ')
    };

    try {
        const res = await fetch('/api/ve-xe/dat-ve', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await res.json();
        if (!res.ok) {
            throw new Error(result.message || 'Lỗi khi đặt vé');
        }

        // Đóng modal sơ đồ ghế
        bootstrap.Modal.getInstance(document.getElementById('seatMapModal')).hide();
        showToast('Đặt vé thành công! Đang xuất phiếu vé điện tử...', 'success');

        // Hiển thị phiếu vé điện tử
        displayETicket(result.data);

        // Tải lại danh sách chuyến xe
        loadAllTrips();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function displayETicket(tickets) {
    if (!tickets || tickets.length === 0) return;
    const firstTicket = tickets[0];
    const trip = firstTicket.chuyenXe || {};
    const seatsList = tickets.map(t => t.soGhe).join(', ');
    const totalAmount = tickets.reduce((acc, t) => acc + (t.giaVe || 0), 0);

    const ticketHtml = `
        <div class="ticket-header text-center">
            <h4 class="fw-bold text-success mb-1"><i class="fa-solid fa-qrcode me-2"></i>PHIẾU VÉ XE ĐIỆN TỬ</h4>
            <div class="text-muted small">BẾN XE KHÁCH TRUNG TÂM HÀ NỘI</div>
            <div class="badge bg-success fs-6 mt-2">${firstTicket.maVeCode}</div>
        </div>
        <div class="row g-2 mb-3">
            <div class="col-6">
                <span class="text-muted small">Hành khách:</span>
                <div class="fw-bold">${firstTicket.tenKhachHang}</div>
            </div>
            <div class="col-6 text-end">
                <span class="text-muted small">Số điện thoại:</span>
                <div class="fw-bold">${firstTicket.soDienThoai}</div>
            </div>
            <div class="col-12">
                <span class="text-muted small">Tuyến đường:</span>
                <div class="fw-bold text-primary">${trip.tenTuyen || 'Tuyến liên tỉnh'}</div>
            </div>
            <div class="col-6">
                <span class="text-muted small">Giờ xuất bến:</span>
                <div class="fw-bold">${formatDateTime(trip.khoiHanh)}</div>
            </div>
            <div class="col-6 text-end">
                <span class="text-muted small">Số ghế giữ chỗ:</span>
                <div class="fw-bold text-danger fs-5">${seatsList}</div>
            </div>
            <div class="col-6">
                <span class="text-muted small">Phương tiện:</span>
                <div>${trip.bienSoXe || 'Xe khách'} (${trip.loaiXe || ''})</div>
            </div>
            <div class="col-6 text-end">
                <span class="text-muted small">Tổng tiền đã thanh toán:</span>
                <div class="fw-bold text-success fs-5">${formatVND(totalAmount)}</div>
            </div>
        </div>
        <div class="alert alert-light border small text-muted mb-0">
            <i class="fa-solid fa-circle-info text-info me-1"></i> Quý khách vui lòng có mặt tại bến xe trước giờ xuất bến 15 phút và xuất trình mã vé này cho tài xế hoặc ban quản lý bến.
        </div>
    `;

    document.getElementById('e-ticket-content').innerHTML = ticketHtml;
    const modal = new bootstrap.Modal(document.getElementById('eTicketModal'));
    modal.show();
}

// ==============================================================
// 3. TAB 2: BAN QUẢN LÝ BẾN XE (ADMIN DASHBOARD)
// ==============================================================

async function loadAdminDashboard() {
    try {
        // Tải số liệu KPI
        const resStats = await fetch('/api/thong-ke/ben-xe');
        const stats = await resStats.json();

        document.getElementById('kpi-tong-xe').textContent = stats.tongSoXe;
        document.getElementById('kpi-con-o-ben').textContent = stats.conOBen;
        document.getElementById('kpi-xe-den').textContent = stats.xeDen;
        document.getElementById('kpi-xe-di').textContent = stats.xeDi;
        document.getElementById('kpi-tong-nha-xe').textContent = stats.tongNhaXe;
        document.getElementById('kpi-tong-ve').textContent = stats.tongVeDaBan;
        document.getElementById('kpi-tong-doanh-thu').textContent = formatVND(stats.tongDoanhThu);

        // Tải danh sách xe bến bãi
        loadAdminVehicles();

        // Tải danh sách nhà xe
        loadAdminNhaXes();
    } catch (err) {
        console.error('Lỗi tải Admin dashboard:', err);
    }
}

async function loadAdminVehicles() {
    try {
        const res = await fetch('/api/xe');
        const data = await res.json();
        state.vehicles = data;
        renderAdminVehiclesTable(data);
    } catch (err) {
        console.error(err);
    }
}

function filterAdminVehicles() {
    const status = document.getElementById('admin-filter-xe-status').value;
    const filtered = status ? state.vehicles.filter(v => v.trangThai === status) : state.vehicles;
    renderAdminVehiclesTable(filtered);
}

function renderAdminVehiclesTable(vehicles) {
    const tbody = document.getElementById('admin-vehicles-table');
    if (!tbody) return;

    if (vehicles.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted py-4">Không có phương tiện nào trong danh sách.</td></tr>`;
        return;
    }

    tbody.innerHTML = vehicles.map(v => {
        let badgeClass = 'badge-con-o-ben';
        let label = 'Còn ở bến';
        if (v.trangThai === 'XE_DEN') { badgeClass = 'badge-xe-den'; label = 'Xe đến bến'; }
        if (v.trangThai === 'XE_DI') { badgeClass = 'badge-xe-di'; label = 'Xe xuất bến'; }
        if (v.trangThai === 'BAO_TRI') { badgeClass = 'badge-bao-tri'; label = 'Bảo trì kỹ thuật'; }

        // Tìm tên nhà xe
        const nx = state.nhaxes.find(n => n.maUser === v.maNhaXe);
        const tenNx = nx ? nx.tenNhaXe : `Nhà xe #${v.maNhaXe}`;

        return `
            <tr>
                <td class="fw-bold">#${v.maXe}</td>
                <td><span class="badge bg-dark fs-6 font-monospace">${v.bienSo}</span></td>
                <td>${v.loaiXe}</td>
                <td>${v.soCho} chỗ</td>
                <td><span class="fw-semibold text-primary">${tenNx}</span></td>
                <td><span class="badge-status ${badgeClass}"><i class="fa-solid fa-circle fa-2xs"></i> ${label}</span></td>
                <td>
                    <div class="dropdown">
                        <button class="btn btn-outline-secondary btn-sm dropdown-toggle py-1 px-2" type="button" data-bs-toggle="dropdown">
                            Điều độ trạng thái
                        </button>
                        <ul class="dropdown-menu shadow-sm">
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="quickUpdateXeStatus(${v.maXe}, 'CON_O_BEN')"><i class="fa-solid fa-square-parking me-2 text-info"></i>Còn ở bến (Lưu bến)</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="quickUpdateXeStatus(${v.maXe}, 'XE_DEN')"><i class="fa-solid fa-arrow-right-to-bracket me-2 text-success"></i>Cập bến (Xe đến)</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="quickUpdateXeStatus(${v.maXe}, 'XE_DI')"><i class="fa-solid fa-arrow-right-from-bracket me-2 text-warning"></i>Xuất bến (Xe đi)</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="quickUpdateXeStatus(${v.maXe}, 'BAO_TRI')"><i class="fa-solid fa-wrench me-2 text-danger"></i>Đưa vào bảo dưỡng</a></li>
                        </ul>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

async function quickUpdateXeStatus(xeId, newStatus) {
    try {
        const res = await fetch(`/api/xe/${xeId}/trang-thai`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ trangThai: newStatus })
        });
        if (!res.ok) throw new Error('Không thể cập nhật trạng thái xe');
        showToast('Cập nhật trạng thái điều độ xe thành công!', 'success');
        loadAdminDashboard();
        if (state.currentRole === 'NHA_XE') loadOperatorPortal();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function loadAdminNhaXes() {
    try {
        const res = await fetch('/api/nha-xe');
        const data = await res.json();
        state.nhaxes = data;
        renderAdminNhaXeTable(data);
        populateOperatorNhaXeSelect(data);
    } catch (err) {
        console.error(err);
    }
}

function renderAdminNhaXeTable(nhaxes) {
    const tbody = document.getElementById('admin-nhaxe-table');
    if (!tbody) return;

    tbody.innerHTML = nhaxes.map(n => {
        const isLocked = n.trangThai === 'KHOA';
        return `
            <tr>
                <td class="fw-bold">#${n.maUser}</td>
                <td><div class="fw-bold text-primary">${n.tenNhaXe}</div><div class="small text-muted">${n.moTa || ''}</div></td>
                <td>${n.soDienThoai || 'N/A'}</td>
                <td>${n.diaChi || 'N/A'}</td>
                <td>
                    <span class="badge ${isLocked ? 'bg-danger' : 'bg-success'}">
                        ${isLocked ? 'Đã khóa' : 'Hoạt động'}
                    </span>
                </td>
                <td>
                    <div class="d-flex gap-1">
                        <button class="btn btn-outline-warning btn-sm" onclick="toggleLockNhaXe(${n.maUser}, '${n.trangThai}')" title="${isLocked ? 'Mở khóa' : 'Khóa'}">
                            <i class="fa-solid ${isLocked ? 'fa-lock-open' : 'fa-lock'}"></i>
                        </button>
                        <button class="btn btn-outline-danger btn-sm" onclick="handleDeleteNhaXe(${n.maUser})" title="Xóa nhà xe">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

function openCreateNhaXeModal() {
    document.getElementById('nhaxeModalTitle').textContent = 'Đăng Ký Nhà Xe Mới';
    document.getElementById('nhaxe-form-id').value = '';
    document.getElementById('nhaxe-account-group').style.display = 'block';
    document.getElementById('nhaxe-pass-group').style.display = 'block';
    document.getElementById('nhaxe-username').value = '';
    document.getElementById('nhaxe-password').value = '';
    document.getElementById('nhaxe-ten').value = '';
    document.getElementById('nhaxe-sdt').value = '';
    document.getElementById('nhaxe-diachi').value = '';
    document.getElementById('nhaxe-mota').value = '';

    new bootstrap.Modal(document.getElementById('nhaxeModal')).show();
}

async function handleSaveNhaXe(e) {
    e.preventDefault();
    const id = document.getElementById('nhaxe-form-id').value;
    const username = document.getElementById('nhaxe-username').value;
    const password = document.getElementById('nhaxe-password').value;
    const tenNhaXe = document.getElementById('nhaxe-ten').value;
    const soDienThoai = document.getElementById('nhaxe-sdt').value;
    const diaChi = document.getElementById('nhaxe-diachi').value;
    const moTa = document.getElementById('nhaxe-mota').value;

    try {
        if (!id) {
            // Đăng ký mới qua Auth API để tạo đồng bộ cả User và Nhà xe
            const res = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    username,
                    password,
                    tenNhaXe,
                    soDienThoai,
                    diaChi,
                    vaiTro: 'NHA_XE'
                })
            });
            const resData = await res.json();
            if (!res.ok) throw new Error(resData.message || 'Lỗi khi đăng ký nhà xe');
            showToast('Đăng ký nhà xe mới thành công!', 'success');
        } else {
            // Cập nhật nhà xe hiện tại
            const res = await fetch(`/api/nha-xe/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ tenNhaXe, soDienThoai, diaChi, moTa })
            });
            if (!res.ok) throw new Error('Lỗi khi cập nhật nhà xe');
            showToast('Cập nhật thông tin nhà xe thành công!', 'success');
        }

        bootstrap.Modal.getInstance(document.getElementById('nhaxeModal')).hide();
        loadAdminNhaXes();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function toggleLockNhaXe(id, currentStatus) {
    const newStatus = currentStatus === 'KHOA' ? 'HOAT_DONG' : 'KHOA';
    try {
        const res = await fetch(`/api/nha-xe/${id}/status`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });
        if (!res.ok) throw new Error('Không thể thay đổi trạng thái');
        showToast(`Đã ${newStatus === 'KHOA' ? 'khóa' : 'mở khóa'} nhà xe!`, 'success');
        loadAdminNhaXes();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleDeleteNhaXe(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa nhà xe này?')) return;
    try {
        const res = await fetch(`/api/nha-xe/${id}`, { method: 'DELETE' });
        if (!res.ok) {
            const data = await res.json();
            throw new Error(data.message || 'Không thể xóa nhà xe (hãy kiểm tra xe và tài xế)');
        }
        showToast('Xóa nhà xe thành công!', 'success');
        loadAdminNhaXes();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// ==============================================================
// 4. TAB 3: QUẢN TRỊ NHÀ XE (OPERATOR PORTAL)
// ==============================================================

function populateOperatorNhaXeSelect(nhaxes) {
    const select = document.getElementById('operator-nhaxe-select');
    if (!select) return;

    select.innerHTML = nhaxes.map(n => `
        <option value="${n.maUser}" ${n.maUser === state.currentNhaXeId ? 'selected' : ''}>
            ${n.tenNhaXe} (#${n.maUser})
        </option>
    `).join('');
}

async function loadOperatorPortal() {
    const select = document.getElementById('operator-nhaxe-select');
    if (select) {
        state.currentNhaXeId = parseInt(select.value) || 1;
    }

    const nxId = state.currentNhaXeId;

    try {
        // 1. Tải danh sách xe của nhà xe này
        const resXe = await fetch(`/api/xe?maNhaXe=${nxId}`);
        const listXe = await resXe.json();
        renderOperatorVehicles(listXe);

        // 2. Tải danh sách tài xế
        const resTx = await fetch(`/api/tai-xe?maNhaXe=${nxId}`);
        const listTx = await resTx.json();
        renderOperatorDrivers(listTx);

        // 3. Tải danh sách chuyến xe
        const resCx = await fetch(`/api/chuyen-xe?maNhaXe=${nxId}`);
        const listCx = await resCx.json();
        renderOperatorTrips(listCx);

        // Cập nhật mini KPIs
        document.getElementById('op-tong-xe').textContent = listXe.length;
        document.getElementById('op-xe-ben').textContent = listXe.filter(x => x.trangThai === 'CON_O_BEN').length;
        document.getElementById('op-taixe-ranh').textContent = listTx.filter(t => t.trangThai === 'RANH').length;
        document.getElementById('op-chuyen-chay').textContent = listCx.filter(c => c.trangThai === 'DANG_CHAY').length;
    } catch (err) {
        console.error('Lỗi nạp cổng nhà xe:', err);
    }
}

function renderOperatorVehicles(vehicles) {
    const tbody = document.getElementById('op-vehicles-table');
    if (!tbody) return;

    if (vehicles.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center text-muted py-4">Nhà xe chưa đăng ký phương tiện nào.</td></tr>`;
        return;
    }

    tbody.innerHTML = vehicles.map(v => {
        let badgeClass = 'badge-con-o-ben';
        let label = 'Còn ở bến';
        if (v.trangThai === 'XE_DEN') { badgeClass = 'badge-xe-den'; label = 'Xe đến'; }
        if (v.trangThai === 'XE_DI') { badgeClass = 'badge-xe-di'; label = 'Xe đi'; }
        if (v.trangThai === 'BAO_TRI') { badgeClass = 'badge-bao-tri'; label = 'Bảo trì'; }

        return `
            <tr>
                <td class="fw-bold">#${v.maXe}</td>
                <td><span class="badge bg-dark font-monospace">${v.bienSo}</span></td>
                <td>${v.loaiXe}</td>
                <td>${v.soCho} chỗ</td>
                <td><span class="badge-status ${badgeClass}"><i class="fa-solid fa-circle fa-2xs"></i> ${label}</span></td>
                <td>
                    <div class="d-flex gap-1">
                        <button class="btn btn-outline-danger btn-sm" onclick="handleDeleteXe(${v.maXe})" title="Xóa xe">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

function renderOperatorDrivers(drivers) {
    const tbody = document.getElementById('op-drivers-table');
    if (!tbody) return;

    if (drivers.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted py-4">Nhà xe chưa có tài xế nào.</td></tr>`;
        return;
    }

    tbody.innerHTML = drivers.map(d => {
        let badgeClass = 'badge-ranh';
        let label = 'Rảnh';
        if (d.trangThai === 'DANG_LAI') { badgeClass = 'badge-dang-lai'; label = 'Đang lái xe'; }
        if (d.trangThai === 'NGHI') { badgeClass = 'badge-nghi'; label = 'Nghỉ phép'; }

        return `
            <tr>
                <td class="fw-bold">#${d.maTaiXe}</td>
                <td class="fw-semibold">${d.hoTen}</td>
                <td>${d.soDienThoai || 'N/A'}</td>
                <td><span class="font-monospace">${d.soGPLX}</span></td>
                <td><span class="badge bg-info text-white">Hạng ${d.loaiBang || 'D'}</span></td>
                <td><span class="badge-status ${badgeClass}"><i class="fa-solid fa-circle fa-2xs"></i> ${label}</span></td>
                <td>
                    <div class="d-flex gap-1">
                        <button class="btn btn-outline-danger btn-sm" onclick="handleDeleteTaiXe(${d.maTaiXe})" title="Xóa tài xế">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

function renderOperatorTrips(trips) {
    const tbody = document.getElementById('op-trips-table');
    if (!tbody) return;

    if (trips.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" class="text-center text-muted py-4">Chưa có chuyến xe nào được lập lịch.</td></tr>`;
        return;
    }

    tbody.innerHTML = trips.map(t => {
        let badge = `<span class="badge bg-success">Sắp khởi hành</span>`;
        if (t.trangThai === 'DANG_CHAY') badge = `<span class="badge bg-primary">Đang chạy</span>`;
        if (t.trangThai === 'HOAN_THANH') badge = `<span class="badge bg-secondary">Hoàn thành</span>`;
        if (t.trangThai === 'DA_HUY') badge = `<span class="badge bg-danger">Đã hủy</span>`;

        return `
            <tr>
                <td class="fw-bold">#${t.maChuyen}</td>
                <td class="fw-semibold text-primary">${t.tenTuyen || 'Tuyến'}</td>
                <td>${t.bienSoXe || 'Xe'}</td>
                <td>${t.tenTaiXe || 'Tài xế'}</td>
                <td>${formatDateTime(t.khoiHanh)}</td>
                <td class="fw-bold text-danger">${formatVND(t.giaVe)}</td>
                <td><span class="badge bg-light text-dark border">${t.soGheTrong} chỗ</span></td>
                <td>${badge}</td>
                <td>
                    <div class="dropdown">
                        <button class="btn btn-sm btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown">
                            Thao tác
                        </button>
                        <ul class="dropdown-menu shadow-sm">
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="updateTripStatus(${t.maChuyen}, 'DANG_CHAY')"><i class="fa-solid fa-play me-2 text-primary"></i>Bắt đầu chạy</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="updateTripStatus(${t.maChuyen}, 'HOAN_THANH')"><i class="fa-solid fa-flag-checkered me-2 text-success"></i>Hoàn thành chuyến</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="updateTripStatus(${t.maChuyen}, 'DA_HUY')"><i class="fa-solid fa-ban me-2 text-danger"></i>Hủy chuyến</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="javascript:void(0)" onclick="handleDeleteChuyenXe(${t.maChuyen})"><i class="fa-solid fa-trash me-2"></i>Xóa chuyến xe</a></li>
                        </ul>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

// Modal Thêm Xe Mới
function openCreateXeModal() {
    document.getElementById('xe-form-id').value = '';
    document.getElementById('xe-bienso').value = '';
    document.getElementById('xe-socho').value = '40';
    document.getElementById('xe-trangthai').value = 'CON_O_BEN';
    new bootstrap.Modal(document.getElementById('xeModal')).show();
}

async function handleSaveXe(e) {
    e.preventDefault();
    const bienSo = document.getElementById('xe-bienso').value;
    const loaiXe = document.getElementById('xe-loaixe').value;
    const soCho = parseInt(document.getElementById('xe-socho').value);
    const trangThai = document.getElementById('xe-trangthai').value;

    const payload = {
        bienSo,
        loaiXe,
        soCho,
        trangThai,
        maNhaXe: state.currentNhaXeId
    };

    try {
        const res = await fetch('/api/xe', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Lỗi thêm xe');

        showToast('Thêm phương tiện mới thành công!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('xeModal')).hide();
        loadOperatorPortal();
        loadAdminDashboard();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleDeleteXe(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa xe này khỏi đội xe?')) return;
    try {
        const res = await fetch(`/api/xe/${id}`, { method: 'DELETE' });
        if (!res.ok) {
            const data = await res.json();
            throw new Error(data.message || 'Không thể xóa xe');
        }
        showToast('Xóa xe thành công!', 'success');
        loadOperatorPortal();
        loadAdminDashboard();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Modal Thêm Tài Xế Mới
function openCreateTaiXeModal() {
    document.getElementById('taixe-form-id').value = '';
    document.getElementById('taixe-hoten').value = '';
    document.getElementById('taixe-sdt').value = '';
    document.getElementById('taixe-gplx').value = '';
    document.getElementById('taixe-hangbang').value = 'E';
    document.getElementById('taixe-trangthai').value = 'RANH';
    new bootstrap.Modal(document.getElementById('taiXeModal')).show();
}

async function handleSaveTaiXe(e) {
    e.preventDefault();
    const hoTen = document.getElementById('taixe-hoten').value;
    const soDienThoai = document.getElementById('taixe-sdt').value;
    const soGPLX = document.getElementById('taixe-gplx').value;
    const loaiBang = document.getElementById('taixe-hangbang').value;
    const trangThai = document.getElementById('taixe-trangthai').value;

    const payload = {
        hoTen,
        soDienThoai,
        soGPLX,
        loaiBang,
        trangThai,
        maNhaXe: state.currentNhaXeId
    };

    try {
        const res = await fetch('/api/tai-xe', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Lỗi thêm tài xế');

        showToast('Thêm hồ sơ tài xế thành công!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('taiXeModal')).hide();
        loadOperatorPortal();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleDeleteTaiXe(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa hồ sơ tài xế này?')) return;
    try {
        const res = await fetch(`/api/tai-xe/${id}`, { method: 'DELETE' });
        if (!res.ok) {
            const data = await res.json();
            throw new Error(data.message || 'Không thể xóa tài xế');
        }
        showToast('Xóa tài xế thành công!', 'success');
        loadOperatorPortal();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Modal Tạo Chuyến Xe Mới
async function openCreateChuyenXeModal() {
    try {
        // Tải tuyến đường
        const resTuyen = await fetch('/api/tuyen-xe');
        const tuyenList = await resTuyen.json();
        document.getElementById('cx-form-tuyen').innerHTML = tuyenList.map(t => `
            <option value="${t.maTuyen}">${t.tenTuyen} (${t.khoangCachKm} km)</option>
        `).join('');

        // Tải danh sách xe của nhà xe
        const resXe = await fetch(`/api/xe?maNhaXe=${state.currentNhaXeId}`);
        const xeList = await resXe.json();
        document.getElementById('cx-form-xe').innerHTML = xeList.map(x => `
            <option value="${x.maXe}">${x.bienSo} - ${x.loaiXe} (${x.trangThai === 'CON_O_BEN' ? 'Tại bến' : x.trangThai})</option>
        `).join('');

        // Tải danh sách tài xế của nhà xe
        const resTx = await fetch(`/api/tai-xe?maNhaXe=${state.currentNhaXeId}`);
        const txList = await resTx.json();
        document.getElementById('cx-form-taixe').innerHTML = txList.map(t => `
            <option value="${t.maTaiXe}">${t.hoTen} - GPLX: ${t.soGPLX} (${t.trangThai === 'RANH' ? 'Rảnh' : t.trangThai})</option>
        `).join('');

        // Mặc định ngày giờ khởi hành: 2 tiếng nữa
        const now = new Date();
        now.setHours(now.getHours() + 2);
        now.setMinutes(0);
        document.getElementById('cx-form-gio-di').value = now.toISOString().slice(0, 16);

        new bootstrap.Modal(document.getElementById('chuyenXeModal')).show();
    } catch (err) {
        showToast('Lỗi khi mở form tạo chuyến xe: ' + err.message, 'error');
    }
}

async function handleSaveChuyenXe(e) {
    e.preventDefault();
    const maTuyen = parseInt(document.getElementById('cx-form-tuyen').value);
    const maXe = parseInt(document.getElementById('cx-form-xe').value);
    const maTaiXe = parseInt(document.getElementById('cx-form-taixe').value);
    const khoiHanh = document.getElementById('cx-form-gio-di').value;
    const giaVe = parseInt(document.getElementById('cx-form-gia-ve').value);

    const payload = {
        maNhaXe: state.currentNhaXeId,
        maTuyen,
        maXe,
        maTaiXe,
        khoiHanh: khoiHanh + ':00',
        giaVe,
        trangThai: 'SAP_KHOI_HANH'
    };

    try {
        const res = await fetch('/api/chuyen-xe', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Lỗi tạo chuyến xe');

        showToast('Lập lịch chuyến xe mới thành công!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('chuyenXeModal')).hide();
        loadOperatorPortal();
        loadAllTrips();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function updateTripStatus(tripId, status) {
    try {
        const res = await fetch(`/api/chuyen-xe/${tripId}/trang-thai`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ trangThai: status })
        });
        if (!res.ok) throw new Error('Không thể đổi trạng thái chuyến');
        showToast('Đã cập nhật trạng thái chuyến xe!', 'success');
        loadOperatorPortal();
        loadAllTrips();
        loadAdminDashboard();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleDeleteChuyenXe(tripId) {
    if (!confirm('Bạn có chắc muốn xóa chuyến xe này?')) return;
    try {
        const res = await fetch(`/api/chuyen-xe/${tripId}`, { method: 'DELETE' });
        if (!res.ok) {
            const data = await res.json();
            throw new Error(data.message || 'Không thể xóa chuyến xe');
        }
        showToast('Xóa chuyến xe thành công!', 'success');
        loadOperatorPortal();
        loadAllTrips();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// ==============================================================
// 5. TAB 4: TRA CỨU VÉ & ĐÁNH GIÁ
// ==============================================================

async function handleSearchTickets() {
    const q = document.getElementById('search-ticket-input').value.trim();
    if (!q) {
        showToast('Vui lòng nhập mã vé hoặc số điện thoại', 'error');
        return;
    }

    try {
        const res = await fetch(`/api/ve-xe/tra-cuu?q=${encodeURIComponent(q)}`);
        const tickets = await res.json();
        renderTicketResults(tickets);
    } catch (err) {
        showToast('Lỗi khi tra cứu vé', 'error');
    }
}

function renderTicketResults(tickets) {
    const container = document.getElementById('ticket-search-results');
    if (!container) return;

    if (tickets.length === 0) {
        container.innerHTML = `
            <div class="alert alert-warning">
                <i class="fa-solid fa-triangle-exclamation me-2"></i>Không tìm thấy vé nào phù hợp với thông tin đã nhập.
            </div>
        `;
        return;
    }

    container.innerHTML = tickets.map(t => {
        const isCancelled = t.trangThai === 'DA_HUY';
        const trip = t.chuyenXe || {};

        return `
            <div class="card mb-3 border shadow-sm">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <div>
                            <span class="badge bg-primary fs-6 font-monospace">${t.maVeCode}</span>
                            <span class="badge ${isCancelled ? 'bg-danger' : 'bg-success'} ms-1">
                                ${isCancelled ? 'ĐÃ HỦY VÉ' : 'HỢP LỆ (ĐÃ ĐẶT)'}
                            </span>
                        </div>
                        <div class="fw-bold text-danger fs-5">${formatVND(t.giaVe)}</div>
                    </div>
                    <div class="row g-2 small">
                        <div class="col-sm-6">
                            <span class="text-muted">Hành khách:</span> <strong>${t.tenKhachHang}</strong> (${t.soDienThoai})
                        </div>
                        <div class="col-sm-6 text-sm-end">
                            <span class="text-muted">Số ghế giữ chỗ:</span> <strong class="text-primary fs-6">${t.soGhe}</strong>
                        </div>
                        <div class="col-sm-6">
                            <span class="text-muted">Tuyến đường:</span> <strong>${trip.tenTuyen || 'Tuyến'}</strong>
                        </div>
                        <div class="col-sm-6 text-sm-end">
                            <span class="text-muted">Xuất bến:</span> <strong>${formatDateTime(trip.khoiHanh)}</strong>
                        </div>
                    </div>
                    ${!isCancelled ? `
                        <div class="mt-3 text-end">
                            <button class="btn btn-outline-danger btn-sm" onclick="handleCancelTicket(${t.maVe})">
                                <i class="fa-solid fa-ban me-1"></i> Yêu Cầu Hủy Vé
                            </button>
                        </div>
                    ` : ''}
                </div>
            </div>
        `;
    }).join('');
}

async function handleCancelTicket(ticketId) {
    if (!confirm('Bạn có chắc chắn muốn hủy chiếc vé này? Ghế sẽ được trả lại cho hành khách khác.')) return;
    try {
        const res = await fetch(`/api/ve-xe/${ticketId}/huy`, { method: 'PATCH' });
        if (!res.ok) throw new Error('Không thể hủy vé');
        showToast('Đã hủy vé thành công!', 'success');
        handleSearchTickets();
        loadAllTrips();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Đánh giá Chuyến Xe
function populateReviewTripsSelect(trips) {
    const select = document.getElementById('review-chuyen-xe-select');
    if (!select) return;

    select.innerHTML = trips.map(t => `
        <option value="${t.maChuyen}">
            #${t.maChuyen} - ${t.tenTuyen || 'Chuyến xe'} (${formatDateTime(t.khoiHanh)})
        </option>
    `).join('');
}

async function handleSendReview(e) {
    e.preventDefault();
    const maChuyen = parseInt(document.getElementById('review-chuyen-xe-select').value);
    const tenKhachHang = document.getElementById('review-ten-kh').value;
    const soSao = parseInt(document.getElementById('review-so-sao').value);
    const noiDung = document.getElementById('review-noi-dung').value;

    try {
        const res = await fetch('/api/danh-gia', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                maChuyen,
                tenKhachHang,
                soSao,
                noiDung
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Lỗi gửi đánh giá');

        showToast('Cảm ơn bạn đã gửi đánh giá cho bến xe!', 'success');
        document.getElementById('review-noi-dung').value = '';
        loadRecentReviews();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function loadRecentReviews() {
    try {
        const res = await fetch('/api/danh-gia');
        const list = await res.json();
        const container = document.getElementById('recent-reviews-container');
        if (!container) return;

        if (list.length === 0) {
            container.innerHTML = `<div class="text-muted small">Chưa có đánh giá nào.</div>`;
            return;
        }

        container.innerHTML = list.slice(-3).reverse().map(r => `
            <div class="p-3 bg-light rounded-3 mb-2 small">
                <div class="d-flex justify-content-between align-items-center mb-1">
                    <strong class="text-dark">${r.tenKhachHang}</strong>
                    <span class="text-warning">${'⭐'.repeat(r.soSao)}</span>
                </div>
                <div class="text-muted">${r.noiDung || 'Dịch vụ tốt.'}</div>
            </div>
        `).join('');
    } catch (err) {
        console.error(err);
    }
}

// ==============================================================
// 6. INITIALIZATION
// ==============================================================

document.addEventListener('DOMContentLoaded', () => {
    // 1. Tải danh sách chuyến xe ban đầu
    loadAllTrips();

    // 2. Tải số liệu Admin
    loadAdminDashboard();

    // 3. Tải cổng nhà xe ban đầu
    loadOperatorPortal();

    // 4. Tải các đánh giá gần đây
    loadRecentReviews();

    // Thiết lập ngày mặc định cho bộ lọc: hôm nay
    const today = new Date().toISOString().split('T')[0];
    const dateInput = document.getElementById('search-ngay-di');
    if (dateInput) dateInput.value = today;
});
