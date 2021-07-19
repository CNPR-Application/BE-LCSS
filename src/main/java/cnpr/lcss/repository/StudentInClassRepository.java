package cnpr.lcss.repository;

import cnpr.lcss.dao.StudentInClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInClassRepository extends JpaRepository<StudentInClass, Integer> {

    @Query(value = "SELECT COUNT(sic.studentInClassId) " +
            "FROM StudentInClass AS sic " +
            "WHERE sic.aClass.classId = :classId")
    int countStudentInClassByAClass_ClassId(@Param(value = "classId") int classId);

    Page<StudentInClass> findStudentInClassByaClass_ClassId(int classId, Pageable pageable);
}
