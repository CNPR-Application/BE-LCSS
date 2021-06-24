package cnpr.lcss.repository;

import cnpr.lcss.dao.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    Shift findShiftByShiftId(int shiftId);
}
