'use strict';

// call api
async function goiApi(url, phuongThuc, duLieu) {
    const tuyChon = { method: phuongThuc || 'GET', headers: { 'Accept': 'application/json' } };
    if (duLieu !== undefined) {
        tuyChon.headers['Content-Type'] = 'application/json';
        tuyChon.body = JSON.stringify(duLieu);
    }
    let res;
    try {
        res = await fetch(url, tuyChon);
    } catch (e) {
        return { ok: false, status: 0, body: { message: 'Không kết nối được máy chủ. Hãy kiểm tra ứng dụng đã chạy chưa.' } };
    }
    let body = null;
    try { body = await res.json(); } catch (e) { body = null; }
    return { ok: res.ok, status: res.status, body: body || {} };
}

function thoat(s) {
    if (s === null || s === undefined) return '';
    return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function dinhDangTien(so) {
    if (so === null || so === undefined) return '';
    return Number(so).toLocaleString('vi-VN') + ' đ';
}

// dinh dang hh:mm
function dinhDangGio(gio) {
    return gio ? String(gio).substring(0, 5) : '';
}

// dinh dang dd/mm/yy
function dinhDangNgay(ngay) {
    if (!ngay) return '';
    const p = String(ngay).split('-');
    return p[2] + '/' + p[1] + '/' + p[0];
}

function nhanTrangThai(tt) {
    const ten = { ChuaChay: 'Chưa chạy', DangChay: 'Đang chạy', HoanThanh: 'Hoàn thành', Huy: 'Đã hủy' };
    return '<span class="nhan nhan-' + thoat(tt) + '">' + thoat(ten[tt] || tt) + '</span>';
}

function layThamSo(ten) {
    return new URLSearchParams(location.search).get(ten);
}

// hien thong bao
function hienThongBao(noiDung, loai) {
    const el = document.getElementById('thongbao');
    if (!el) return;
    el.className = 'thongbao ' + (loai || 'loi');
    el.textContent = noiDung;
    el.hidden = false;
}