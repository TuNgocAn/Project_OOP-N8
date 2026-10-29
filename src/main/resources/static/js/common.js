'use strict';

/* Các hàm dùng chung cho cả 3 trang */

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
const PHONE_RE = /^(0|\+84)\d{9}$/;
const USERNAME_RE = /^[A-Za-z0-9_.]{4,50}$/;

/** Gọi API (cùng origin nên trình duyệt tự gửi cookie phiên). */
async function apiFetch(url, options) {
  options = options || {};
  const init = {
    method: options.method || 'GET',
    credentials: 'same-origin',
    headers: { 'Accept': 'application/json' }
  };
  if (options.data !== undefined) {
    init.headers['Content-Type'] = 'application/json';
    init.body = JSON.stringify(options.data);
  }
  let res;
  try {
    res = await fetch(url, init);
  } catch (e) {
    return { ok: false, status: 0, body: { message: 'Không kết nối được máy chủ. Hãy kiểm tra mạng rồi thử lại.' } };
  }
  let body = null;
  try { body = await res.json(); } catch (e) { body = null; }
  return { ok: res.ok, status: res.status, body: body || {} };
}

function showAlert(el, message, kind) {
  el.className = 'alert alert-' + (kind || 'error');
  el.textContent = message;
  el.hidden = false;
}

function clearAlert(el) {
  el.hidden = true;
  el.textContent = '';
}

function setFieldError(form, name, message) {
  const input = form.elements[name];
  const out = form.querySelector('[data-error-for="' + name + '"]');
  if (out) out.textContent = message || '';
  if (input) {
    if (message) input.setAttribute('aria-invalid', 'true');
    else input.removeAttribute('aria-invalid');
  }
}

function clearFieldErrors(form) {
  form.querySelectorAll('[data-error-for]').forEach(function (n) {
    setFieldError(form, n.getAttribute('data-error-for'), '');
  });
}

/** Hiện lỗi theo từng ô; trả về true nếu có ít nhất một lỗi khớp với ô trên form. */
function applyFieldErrors(form, errors) {
  let any = false;
  Object.keys(errors || {}).forEach(function (name) {
    if (form.querySelector('[data-error-for="' + name + '"]')) {
      setFieldError(form, name, errors[name]);
      any = true;
    }
  });
  return any;
}

function focusFirstInvalid(form) {
  const el = form.querySelector('[aria-invalid="true"]');
  if (el) el.focus();
}

/** Gõ lại vào ô nào thì xoá lỗi của ô đó. */
function bindLiveClear(form) {
  form.addEventListener('input', function (e) {
    if (e.target && e.target.name) setFieldError(form, e.target.name, '');
  });
}

function setBusy(btn, busy, busyText) {
  if (busy) {
    btn.dataset.label = btn.textContent;
    btn.textContent = busyText;
    btn.disabled = true;
  } else {
    btn.textContent = btn.dataset.label || btn.textContent;
    btn.disabled = false;
  }
}

/** Nút Hiện/Ẩn mật khẩu. */
function initPasswordToggles() {
  document.querySelectorAll('.toggle').forEach(function (btn) {
    const input = btn.parentElement.querySelector('input');
    btn.addEventListener('click', function () {
      const show = input.type === 'password';
      input.type = show ? 'text' : 'password';
      btn.textContent = show ? 'Ẩn' : 'Hiện';
      btn.setAttribute('aria-pressed', String(show));
      btn.setAttribute('aria-label', show ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');
    });
  });
}

function todayLocal() {
  const d = new Date();
  const pad = function (n) { return String(n).padStart(2, '0'); };
  return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
}

function toast(message) {
  let el = document.getElementById('toast');
  if (!el) return;
  el.textContent = message;
  el.classList.add('show');
  clearTimeout(toast._t);
  toast._t = setTimeout(function () { el.classList.remove('show'); }, 2600);
}

/** Lấy thông báo lỗi để hiện ở đầu form. */
function errorMessage(res, fallback) {
  return (res.body && res.body.message) || fallback;
}
