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
    //<editor-fold desc="Page<Attendance> findStudentAttendanceInAClass">
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
    //</editor-fold>

    //<editor-fold desc="Page<Attendance> findAllStudentAttendanceInASession">
    @Query(
            nativeQuery = true,
            countQuery = "SELECT count(a.attendance_id) " +
                    "FROM attendance AS a " +
                    "INNER JOIN student_in_class AS sic ON a.student_class_id = sic.student_class_id " +
                    "INNER JOIN student AS s ON sic.student_id = s.student_id " +
                    "INNER JOIN account AS acc on s.student_username = acc.username " +
                    "WHERE a.session_id = :sessionId ",
            value = "SELECT a.attendance_id, a.checking_date, a.status, a.session_id, a.student_class_id, " +
                    "sic.student_id, " +
                    "s.student_username, " +
                    "acc.name, acc.image " +
                    "FROM attendance AS a " +
                    "INNER JOIN student_in_class AS sic ON a.student_class_id = sic.student_class_id " +
                    "INNER JOIN student AS s ON sic.student_id = s.student_id " +
                    "INNER JOIN account AS acc on s.student_username = acc.username " +
                    "WHERE a.session_id = :sessionId " +
                    "ORDER BY s.student_username ASC"
    )
    Page<Attendance> findAllStudentAttendanceInASession(@Param(value = "sessionId") int sessionId,
                                                        Pageable pageable);
    //</editor-fold>
}
