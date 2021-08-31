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

    @Query(value = "SELECT * " +
            "FROM [Language Center].dbo.session AS s " +
            "JOIN [Language Center].dbo.class c ON c.class_id = s.class_id " +
            "WHERE (DATEPART(yy, s.start_time) = 2021 " +
            "AND DATEPART(mm, s.start_time) = 09 " +
            "AND DATEPART(dd, s.start_time) = 01)",
            nativeQuery = true)
    Page<Session> findByStartTimeAndAClass_Status(@Param("startTime") Date startTime,
                                                  @Param("status") String status,
                                                  Pageable pageable);

    List<Session> findSessionByaClass_ClassId(int classId);
}