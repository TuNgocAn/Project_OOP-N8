package com.ptit.busmanagement.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> body(HttpStatus s, Object message) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("status", s.value());
        m.put("error", s.getReasonPhrase());
        m.put("message", message);
        return ResponseEntity.status(s).body(m);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFound(NotFoundException e) {
        return body(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> conflict(ConflictException e) {
        return body(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> validation(ValidationException e) {
        return body(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integrity(DataIntegrityViolationException e) {
        return body(HttpStatus.CONFLICT, "Vi phạm ràng buộc dữ liệu (trùng khóa chính, khóa phụ hoặc đang có dữ liệu tham chiếu)");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> invalid(MethodArgumentNotValidException e) {
        Map<String, String> errs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> errs.put(f.getField(), f.getDefaultMessage()));
        return body(HttpStatus.BAD_REQUEST, errs);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> unreadable(HttpMessageNotReadableException e) {
        return body(HttpStatus.BAD_REQUEST, "Body JSON không hợp lệ hoặc sai định dạng trường dữ liệu");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> general(Exception e) {
        return body(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() != null ? e.getMessage() : "Lỗi hệ thống");
    }
}
