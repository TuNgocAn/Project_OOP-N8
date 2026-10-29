'use strict';

(function () {
  const form = document.getElementById('profile-form');
  const pass = document.getElementById('pass');
  const loading = document.getElementById('loading');
  const alertBox = document.getElementById('alert');
  const saveBtn = document.getElementById('save-btn');
  const resetBtn = document.getElementById('reset-btn');
  const logoutBtn = document.getElementById('logout-btn');

  let current = null; // hồ sơ đã lưu gần nhất, dùng cho nút "Hoàn tác"

  bindLiveClear(form);
  form.elements.ngaySinh.max = todayLocal();

  function initials(name) {
    const parts = (name || '').trim().split(/\s+/).filter(Boolean);
    return parts.length ? parts[parts.length - 1].charAt(0).toUpperCase() : '?';
  }

  function formatDate(iso) {
    const d = new Date(iso);
    return isNaN(d.getTime()) ? '' : d.toLocaleDateString('vi-VN');
  }

  function render(p) {
    current = p;
    form.elements.username.value = p.username || '';
    form.elements.email.value = p.email || '';
    form.elements.hoTen.value = p.hoTen || '';
    form.elements.soDienThoai.value = p.soDienThoai || '';
    form.elements.ngaySinh.value = p.ngaySinh || '';
    form.elements.diaChi.value = p.diaChi || '';

    document.getElementById('avatar').textContent = initials(p.hoTen);
    document.getElementById('stub-name').textContent = p.hoTen || p.username;
    document.getElementById('stub-username').textContent = '@' + p.username;
    document.getElementById('stub-since').textContent = p.ngayTao ? 'Tham gia từ ' + formatDate(p.ngayTao) : '';
    document.getElementById('topbar-name').textContent = p.hoTen || p.username;
  }

  async function load() {
    const res = await apiFetch('/api/khach-hang/profile');
    loading.hidden = true;
    if (res.status === 401) { location.replace('login.html'); return; }
    if (!res.ok) {
      showAlert(alertBox, errorMessage(res, 'Chưa tải được hồ sơ. Hãy tải lại trang.'), 'error');
      return;
    }
    render(res.body.data);
    pass.hidden = false;
  }

  function readValues() {
    const optional = function (s) { s = s.trim(); return s === '' ? null : s; };
    return {
      hoTen: form.elements.hoTen.value.trim(),
      email: form.elements.email.value.trim(),
      soDienThoai: optional(form.elements.soDienThoai.value.replace(/\s+/g, '')),
      ngaySinh: optional(form.elements.ngaySinh.value),
      diaChi: optional(form.elements.diaChi.value)
    };
  }

  function validate(d) {
    const e = {};
    if (!d.hoTen) e.hoTen = 'Hãy nhập họ và tên.';
    if (!EMAIL_RE.test(d.email)) e.email = 'Email chưa đúng định dạng.';
    if (d.soDienThoai && !PHONE_RE.test(d.soDienThoai)) e.soDienThoai = 'Số điện thoại gồm 10 số, bắt đầu bằng 0 (hoặc +84).';
    if (d.ngaySinh && d.ngaySinh >= todayLocal()) e.ngaySinh = 'Ngày sinh phải trước hôm nay.';
    if (d.diaChi && d.diaChi.length > 255) e.diaChi = 'Địa chỉ tối đa 255 ký tự.';
    return e;
  }

  form.addEventListener('submit', async function (ev) {
    ev.preventDefault();
    clearAlert(alertBox);
    clearFieldErrors(form);

    const data = readValues();
    const errors = validate(data);
    if (Object.keys(errors).length) {
      applyFieldErrors(form, errors);
      focusFirstInvalid(form);
      return;
    }

    setBusy(saveBtn, true, 'Đang lưu…');
    const res = await apiFetch('/api/khach-hang/profile', { method: 'PUT', data: data });
    setBusy(saveBtn, false);

    if (res.ok) {
      render(res.body.data);
      toast('Đã lưu thay đổi');
      return;
    }
    if (res.status === 401) { location.replace('login.html'); return; }

    const matched = res.body && res.body.errors && applyFieldErrors(form, res.body.errors);
    showAlert(alertBox,
      matched ? 'Vui lòng kiểm tra lại các ô được đánh dấu.' : errorMessage(res, 'Chưa lưu được. Hãy thử lại.'),
      'error');
    focusFirstInvalid(form);
  });

  resetBtn.addEventListener('click', function () {
    clearAlert(alertBox);
    clearFieldErrors(form);
    if (current) render(current);
  });

  logoutBtn.addEventListener('click', async function () {
    setBusy(logoutBtn, true, 'Đang thoát…');
    await apiFetch('/api/auth/logout', { method: 'POST' });
    location.replace('login.html');
  });

  load();
})();
