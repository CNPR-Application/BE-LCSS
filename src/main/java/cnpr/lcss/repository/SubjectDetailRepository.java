package cnpr.lcss.repository;

import cnpr.lcss.dao.SubjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDetailRepository extends JpaRepository<SubjectDetail, Integer> {

    @Query(value = "SELECT " +
            "new SubjectDetail(sd.subjectDetailId, sd.weekNum, sd.weekDescription,sd.learningOutcome) " +
            "FROM SubjectDetail sd " +
            "INNER JOIN Subject s ON sd.subject.subjectId = s.subjectId " +
            "WHERE sd.subject.subjectId = s.subjectId",
            countQuery = "SELECT COUNT (sd.subjectDetailId) " +
                    "FROM SubjectDetail sd " +
                    "INNER JOIN Subject s ON sd.subject.subjectId = s.subjectId " +
                    "WHERE sd.subject.subjectId = s.subjectId")
    Page<SubjectDetail> findSubjectDetailBySubjectId(@Param(value = "subjectId") int subjectId, Pageable pageable);
}