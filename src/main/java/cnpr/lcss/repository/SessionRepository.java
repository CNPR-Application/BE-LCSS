package cnpr.lcss.repository;

import cnpr.lcss.dao.Session;
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
}