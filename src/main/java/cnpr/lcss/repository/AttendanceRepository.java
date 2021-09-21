package cnpr.lcss.repository;

import cnpr.lcss.dao.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    @Query(
            nativeQuery = true,
            value = "SELECT a.attendance_id, a.session_id, a.status, a.checking_date, sic.student_class_id " +
                    "FROM attendance AS a " +
                    "JOIN student_in_class AS sic on a.student_class_id = sic.student_class_id " +
                    "JOIN student s on sic.student_id = s.student_id " +
                    "WHERE s.student_username = :studentUsername AND sic.class_id = :classId " +
                    "ORDER BY a.checking_date ASC"
    )
    Page<Attendance> findStudentAttendanceInAClass(@Param(value = "studentUsername") String studentUsername,
                                                   @Param(value = "classId") int classId,
                                                   Pageable pageable);
}
