package com.javaweb.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;

import com.javaweb.exception.ApiException;

/** Gom các thao tác với HttpSession để controller gọn hơn. */
public final class SessionUtils {

    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    private static final int TIMEOUT_SECONDS = 30 * 60;

    private SessionUtils() {
    }

    /** Tạo phiên mới sau khi đăng nhập (huỷ phiên cũ để tránh session fixation). */
    public static void login(HttpServletRequest request, Integer userId) {
        HttpSession old = request.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(LOGIN_USER_ID, userId);
        session.setMaxInactiveInterval(TIMEOUT_SECONDS);
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /** Lấy mã user đang đăng nhập; chưa đăng nhập thì ném lỗi 401. */
    public static Integer requireUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object id = (session == null) ? null : session.getAttribute(LOGIN_USER_ID);
        if (!(id instanceof Integer)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập hoặc phiên đã hết hạn.");
        }
        return (Integer) id;
    }
}
