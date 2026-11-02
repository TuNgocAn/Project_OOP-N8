package com.ptit.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp NhaXe (Mục 4 & 7):
 * Kế thừa User, quản lý thông tin và các hoạt động của nhà xe đối tác bến bãi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhaXe extends User {
    private String tenNhaXe;
    private String diaChi;
    private String moTa;
    private String avatar;
}
