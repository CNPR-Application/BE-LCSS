package cnpr.lcss.repository;

import cnpr.lcss.dao.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query(value = "SELECT s.session_id, " +
            "s.class_id, c.class_name, s2.subject_id, s2.subject_name, " +
            "s.teacher_id, a.name, a.image, " +
            "s.room_no, " +
            "s.start_time, " +
            "s.end_time " +
            "FROM [Language Center].dbo.session AS s " +
            "JOIN [Language Center].dbo.class c ON s.class_id = c.class_id " +
            "JOIN [Language Center].dbo.subject s2 ON c.subject_id = s2.subject_id " +
            "JOIN [Language Center].dbo.teacher t ON s.teacher_id = t.teacher_id " +
            "JOIN [Language Center].dbo.account a ON t.teacher_username = a.username " +
            "WHERE (DATEPART(yy, s.start_time) = :year " +
            "AND DATEPART(mm, s.start_time) = :month " +
            "AND DATEPART(dd, s.start_time) = :day) " +
            "AND c.status = 'studying'", nativeQuery = true)
    Page<Session> findSessionByDateAndClass_ClassStatusIsStudying(@Param(value = "year") int year,
                                                                  @Param(value = "month") int month,
                                                                  @Param(value = "day") int day,
                                                                  Pageable pageable);
}
