package cnpr.lcss.repository;

import cnpr.lcss.dao.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

//    Page<Shift> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Shift> findAll(Pageable pageable);

//    Shift findShiftByShiftId(int shiftId);

    boolean existsByTimeStartAndDuration(String timeStart, int duration);
}
