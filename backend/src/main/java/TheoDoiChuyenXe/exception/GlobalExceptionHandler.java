package TheoDoiChuyenXe.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Map<String, Object>> xuLyApi(APIException ex) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> xuLyValidate(MethodArgumentNotValidException ex) {
        Map<String, String> loi = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(f -> loi.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Dữ liệu không hợp lệ", "errors", loi));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> xuLyJsonSai(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("message",
                "Dữ liệu gửi lên sai định dạng (ngày: yyyy-MM-dd, giờ: HH:mm:ss, giá vé: số)"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> xuLyRangBuoc(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409).body(Map.of("message",
                "Vi phạm ràng buộc dữ liệu (khóa ngoại hoặc dữ liệu đang được sử dụng)"));
    }
}