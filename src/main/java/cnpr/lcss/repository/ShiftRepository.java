package cnpr.lcss.repository;

import cnpr.lcss.dao.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    Page<Shift> findShiftByDayOfWeekContainingOrTimeStartContaining(String dayOfWeek, String timeStart, Pageable pageable);

    Page<Shift> findAll(Pageable pageable);

    Page<Shift> findByIsAvailable(boolean isAvailable, Pageable pageable);

    Shift findShiftByShiftId(int shiftId);

    boolean existsByDayOfWeekAndTimeStartAndDuration(String dayOfWeek, String timeStart, int duration);

    @Query(value = "SELECT s.isAvailable " +
            "FROM Shift AS s " +
            "WHERE s.shiftId = :shiftId")
    Boolean findIsAvailableByShiftId(@Param(value = "shiftId") int shiftId);

    @Query(value = "SELECT s.timeStart " +
            "FROM Shift AS s " +
            "WHERE s.shiftId = :shiftId")
    String findShift_TimeStartByShiftId(@Param(value = "shiftId") int shiftId);

    @Query(value = "SELECT s.timeEnd " +
            "FROM Shift AS s " +
            "WHERE s.shiftId = :shiftId")
    String findShift_TimeEndByShiftId(@Param(value = "shiftId") int shiftId);

    @Query(value = "SELECT s.dayOfWeek " +
            "FROM Shift AS s " +
            "WHERE s.shiftId = :shiftId")
    String findShift_DayOfWeekByShiftId(@Param(value = "shiftId") int shiftId);
}
