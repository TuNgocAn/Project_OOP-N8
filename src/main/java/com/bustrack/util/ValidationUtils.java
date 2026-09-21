package com.bustrack.util;

import java.util.regex.Pattern;

/**
 * Tiện ích kiểm tra tính hợp lệ của dữ liệu đầu vào (Validation)
 */
public class ValidationUtils {
    // Số điện thoại Việt Nam 10 số bắt đầu bằng 0
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    // Biển số xe mẫu cơ bản
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^[0-9]{2}[A-Z]{1,2}-[0-9]{3,5}(\\.[0-9]{2})?$");

    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidLicensePlate(String plate) {
        if (plate == null || plate.isBlank()) return false;
        return plate.trim().length() >= 7;
    }

    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
