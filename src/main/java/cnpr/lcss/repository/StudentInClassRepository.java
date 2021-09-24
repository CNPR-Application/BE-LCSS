package cnpr.lcss.repository;

import cnpr.lcss.dao.StudentInClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInClassRepository extends JpaRepository<StudentInClass, Integer> {
    @Query(value = "SELECT COUNT(sic.studentInClassId) " +
            "FROM StudentInClass AS sic " +
            "WHERE sic.aClass.classId = :classId")
    int countStudentInClassByAClass_ClassId(@Param(value = "classId") int classId);

    @Query(value = "SELECT new StudentInClass(sic.studentInClassId, sic.teacherRating, sic.subjectRating, sic.feedback) " +
            "FROM StudentInClass AS sic " +
            "WHERE sic.aClass.classId = :classId")
    List<StudentInClass> findStudentsByClassId(@Param(value = "classId") int classId);

    Page<StudentInClass> findStudentInClassByaClass_ClassId(int classId, Pageable pageable);

    List<StudentInClass> findStudentInClassByStudent_Id(int studentId);

    @Query("select distinct s from StudentInClass s where s.student.id = ?1 and s.aClass.classId = ?2")
    StudentInClass findByStudent_IdAndAClass_ClassId(Integer id, int classId);
}
