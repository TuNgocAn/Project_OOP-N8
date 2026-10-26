package TheoDoiChuyenXe.repository;

import TheoDoiChuyenXe.entity.ChuyenXe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChuyenXeRepository extends JpaRepository<ChuyenXe, Integer> {

    //cac chuyen cua 1 xe (chuyen chua huy) trong khoang ngay, tru chuyen dang sua
    @Query("""
            select c from ChuyenXe c
            where c.xe.maXe = :maXe
              and c.ngayKhoiHanh between :tuNgay and :denNgay
              and c.trangThai <> 'Huy'
              and c.maChuyen <> :boQuaMaChuyen
            """)
    List<ChuyenXe> timChuyenCuaXe(@Param("maXe") Integer maXe,
                                  @Param("tuNgay") LocalDate tuNgay,
                                  @Param("denNgay") LocalDate denNgay,
                                  @Param("boQuaMaChuyen") Integer boQuaMaChuyen);

    //tai xe
    @Query("""
            select c from ChuyenXe c
            where c.taiXe.maTaiXe = :maTaiXe
              and c.ngayKhoiHanh between :tuNgay and :denNgay
              and c.trangThai <> 'Huy'
              and c.maChuyen <> :boQuaMaChuyen
            """)
    List<ChuyenXe> timChuyenCuaTaiXe(@Param("maTaiXe") Integer maTaiXe,
                                     @Param("tuNgay") LocalDate tuNgay,
                                     @Param("denNgay") LocalDate denNgay,
                                     @Param("boQuaMaChuyen") Integer boQuaMaChuyen);
}