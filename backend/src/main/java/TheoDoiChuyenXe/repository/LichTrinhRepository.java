package TheoDoiChuyenXe.repository;

import TheoDoiChuyenXe.entity.LichTrinh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LichTrinhRepository extends JpaRepository<LichTrinh, Integer> {

    //co tuyen di
    boolean existsByDiemDiIgnoreCaseAndDiemDenIgnoreCase(String diemDi, String diemDen);

    //co tueyn di, bo qua lich trinh dang sua
    boolean existsByDiemDiIgnoreCaseAndDiemDenIgnoreCaseAndMaLichTrinhNot(
            String diemDi, String diemDen, Integer maLichTrinh);
}