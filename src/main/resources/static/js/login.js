'use strict';

(function () {
  const form = document.getElementById('login-form');
  const alertBox = document.getElementById('alert');
  const submitBtn = document.getElementById('submit-btn');

  initPasswordToggles();
  bindLiveClear(form);

  // Vừa đăng ký xong thì báo cho người dùng
  if (new URLSearchParams(location.search).get('registered') === '1') {
    showAlert(alertBox, 'Đăng ký thành công. Hãy đăng nhập để tiếp tục.', 'success');
  }

  // Đang đăng nhập sẵn thì vào thẳng hồ sơ
  apiFetch('/api/auth/me').then(function (r) {
    if (r.ok) location.replace('profile.html');
  });

  form.addEventListener('submit', async function (e) {
    e.preventDefault();
    clearAlert(alertBox);
    clearFieldErrors(form);

    const username = form.elements.username.value.trim();
    const password = form.elements.password.value;

    let bad = false;
    if (!username) { setFieldError(form, 'username', 'Hãy nhập tên đăng nhập hoặc email.'); bad = true; }
    if (!password) { setFieldError(form, 'password', 'Hãy nhập mật khẩu.'); bad = true; }
    if (bad) { focusFirstInvalid(form); return; }

    setBusy(submitBtn, true, 'Đang đăng nhập…');
    const res = await apiFetch('/api/auth/login', { method: 'POST', data: { username: username, password: password } });
    setBusy(submitBtn, false);

    if (res.ok) {
      location.replace('profile.html');
      return;
    }
    if (res.body && res.body.errors) applyFieldErrors(form, res.body.errors);
    showAlert(alertBox, errorMessage(res, 'Chưa đăng nhập được. Hãy thử lại.'), 'error');
    form.elements.password.value = '';
    form.elements.password.focus();
  });
})();
