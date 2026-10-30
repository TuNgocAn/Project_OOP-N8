package vn.busapp.nhaxe.entity;

import jakarta.persistence.*;
import lombok.*;

/** STUB tối thiểu để kiểm tra FK NHA_XE.MaUser. Thay bằng entity USER của nhóm. */
@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id private Long maUser;
    private String username;
    private String password;
    private String email;
    private String soDienThoai;
    private String trangThai;
}
