package cnpr.lcss.repository;


import cnpr.lcss.dao.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    int countByCurriculum_CurriculumId(int curriculumId);

    List<Subject> findAllByCurriculum_CurriculumId(int curriculumId);

    Subject findBySubjectId(int subjectId);

    Page<Subject> findByCurriculum_CurriculumIdAndIsAvailable(int keyword, boolean isAvailable, Pageable pageable);

    Page<Subject> findBySubjectNameContainingIgnoreCaseAndIsAvailable(String keyword, boolean isAvailable, Pageable pageable);

    Page<Subject> findBySubjectCodeContainingIgnoreCaseAndIsAvailable(String code, boolean isAvailable, Pageable pageable);

    Boolean existsSubjectBySubjectId(int subjectId);

    Boolean existsSubjectBySubjectCode(String subjectCode);

    Boolean existsSubjectBySubjectName(String subjectName);

    Boolean existsSubjectBySubjectNameAndSubjectIdIsNot(String subjectName, int subjectId);

    @Query(value = "SELECT s.isAvailable " +
            "FROM Subject AS s " +
            "WHERE s.subjectId = :subjectId")
    Boolean findIsAvailableBySubjectId(@Param(value = "subjectId") int subjectId);

    @Query(value = "SELECT s.slot " +
            "FROM Subject AS s " +
            "WHERE s.subjectId = :subjectId")
    int findSlotBySubjectId(@Param(value = "subjectId") int subjectId);
}