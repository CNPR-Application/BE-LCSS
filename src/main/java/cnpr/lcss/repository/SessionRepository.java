package cnpr.lcss.repository;

import cnpr.lcss.dao.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    @Query(value = "SELECT s " +
            "FROM Session AS s " +
            "JOIN Class AS c ON c.classId = s.aClass.classId " +
            "WHERE c.status = :status " +
            "AND (s.startTime >= :datetimeStart AND s.endTime <= :datetimeEnd) " +
            "ORDER BY s.startTime ASC ")
    List<Session> findByStartTimeAndAClass_Status(@Param("datetimeStart") Date datetimeStart,
                                                  @Param("datetimeEnd") Date datetimeEnd,
                                                  @Param("status") String status);

    List<Session> findSessionByaClass_ClassId(int classId);

    List<Session> findSessionByTeacher_TeacherId(int teacherId);

    Page<Session> findByaClass_ClassId(int classId, Pageable pageable);

    @Query("select s from Session s where s.sessionId = ?1")
    Session findBySessionId(Integer sessionId);

    @Query(nativeQuery = true,
            value = "select ses.* " +
                    "from session as ses " +
                    "join student_in_class sic on ses.class_id = sic.class_id " +
                    "where (ses.start_time >= ?1 and ses.end_time <= ?2) " +
                    "and sic.student_id = ?3 " +
                    "order by ses.start_time ASC")
    List<Session> findByStartTimeAndEndTimeAndStudentId(Date srchStartTime, Date srchEndTime, int studentId);

    @Query(nativeQuery = true,
            value = "select ses.* " +
                    "from session as ses " +
                    "where (ses.start_time >= ?1 and ses.end_time <= ?2) " +
                    "and ses.teacher_id = ?3 " +
                    "order by ses.start_time ASC")
    List<Session> findByStartTimeAndEndTimeAndTeacherId(Date srchStartTime, Date srchEndTime, int teacherId);
}