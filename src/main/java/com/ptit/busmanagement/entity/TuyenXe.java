package com.ptit.busmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tuyen_xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuyenXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maTuyen;

    @Column(nullable = false, length = 100)
    private String tenTuyen;

    @Column(nullable = false, length = 50)
    private String diemDi;

    @Column(nullable = false, length = 50)
    private String diemDen;

    private Integer thoiGianDuKienPhut;
    private Double khoangCachKm;
}
