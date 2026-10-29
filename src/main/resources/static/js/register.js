'use strict';

(function () {
  const form = document.getElementById('register-form');
  const alertBox = document.getElementById('alert');
  const submitBtn = document.getElementById('submit-btn');

  initPasswordToggles();
  bindLiveClear(form);
  form.elements.ngaySinh.max = todayLocal();

  function readValues() {
    const v = function (name) { return form.elements[name].value; };
    const optional = function (s) { s = s.trim(); return s === '' ? null : s; };
    return {
      username: v('username').trim(),
      email: v('email').trim(),
      password: v('password'),
      confirmPassword: v('confirmPassword'),
      hoTen: v('hoTen').trim(),
      soDienThoai: optional(v('soDienThoai').replace(/\s+/g, '')),
      ngaySinh: optional(v('ngaySinh')),
      diaChi: optional(v('diaChi'))
    };
  }

  function validate(d) {
    const e = {};
    if (!USERNAME_RE.test(d.username)) e.username = 'Dùng 4–50 ký tự: chữ, số, dấu chấm hoặc gạch dưới.';
    if (!EMAIL_RE.test(d.email)) e.email = 'Email chưa đúng định dạng.';
    if (d.password.length < 6 || d.password.length > 64) e.password = 'Mật khẩu cần từ 6 đến 64 ký tự.';
    if (d.confirmPassword !== d.password) e.confirmPassword = 'Mật khẩu xác nhận chưa khớp.';
    if (!d.hoTen) e.hoTen = 'Hãy nhập họ và tên.';
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
      showAlert(alertBox, 'Vui lòng kiểm tra lại các ô được đánh dấu.', 'error');
      focusFirstInvalid(form);
      return;
    }

    setBusy(submitBtn, true, 'Đang tạo tài khoản…');
    const res = await apiFetch('/api/auth/register', { method: 'POST', data: data });

    if (res.ok) {
      showAlert(alertBox, 'Đăng ký thành công. Đang chuyển sang trang đăng nhập…', 'success');
      setTimeout(function () { location.replace('login.html?registered=1'); }, 1000);
      return;
    }

    setBusy(submitBtn, false);
    const matched = res.body && res.body.errors && applyFieldErrors(form, res.body.errors);
    showAlert(alertBox,
      matched ? 'Vui lòng kiểm tra lại các ô được đánh dấu.' : errorMessage(res, 'Chưa đăng ký được. Hãy thử lại.'),
      'error');
    focusFirstInvalid(form);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });
})();
