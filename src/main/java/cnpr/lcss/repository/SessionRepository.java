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
    @Query(
            nativeQuery = true,
            value = "select ses.* " +
                    "from session as ses " +
                    "join class c on ses.class_id = c.class_id " +
                    "where c.status = :status " +
                    "and (ses.start_time >= :datetimeStart and ses.end_time <= :datetimeEnd) " +
                    "and c.branch_id = :branchId " +
                    "order by ses.start_time asc"
    )
    List<Session> findSessionByDateAndBranchIdAndStatus(@Param("datetimeStart") Date datetimeStart,
                                                        @Param("datetimeEnd") Date datetimeEnd,
                                                        @Param("status") String status,
                                                        @Param("branchId") int branchId);

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

    List<Session> findAllByRoom_RoomId(int roomId);

    boolean existsByStartTimeAndTeacher_TeacherIdAndRoom_RoomId(Date startTime, int teacherId, Integer roomId);

    @Query(
            nativeQuery = true,
            value = "select ses.* " +
                    "from session as ses " +
                    "where ses.start_time = :startTime " +
                    "and ses.teacher_id = :teacherId " +
                    "and ses.room_id = :roomId"
    )
    Session findByStartTimeAndTeacherIdAndRoomId(@Param(value = "startTime") Date startTime,
                                                 @Param(value = "teacherId") Integer teacherId,
                                                 @Param(value = "roomId") Integer roomId);

    boolean existsByStartTimeAndTeacher_TeacherId(Date startTime, int teacherId);

    @Query(
            nativeQuery = true,
            value = "select ses.* " +
                    "from session as ses " +
                    "where ses.start_time = :startTime " +
                    "and ses.teacher_id = :teacherId "
    )
    Session findByStartTimeAndTeacherId(@Param(value = "startTime") Date startTime,
                                        @Param(value = "teacherId") Integer teacherId);

    boolean existsByStartTimeAndRoom_RoomId(Date startTime, Integer roomId);

    @Query(
            nativeQuery = true,
            value = "select ses.* " +
                    "from session as ses " +
                    "where ses.start_time = :startTime " +
                    "and ses.room_id = :roomId"
    )
    Session findByStartTimeAndRoomId(@Param(value = "startTime") Date startTime,
                                     @Param(value = "roomId") Integer roomId);

    List<Session> findByStartTime(Date startTime);

    @Query(
            nativeQuery = true,
            value = "select s.*\n" +
                    "from session as s\n" +
                    "where s.room_id = ?1\n" +
                    "  and s.teacher_id = ?2\n" +
                    "  and s.class_id != ?3"
    )
    Session findByRoom_RoomIdAndTeacher_TeacherIdAndAClass_ClassIdIsNot(Integer roomId, int teacherId, int classId);
}