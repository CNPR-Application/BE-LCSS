package cnpr.lcss.repository;

import cnpr.lcss.dao.Student;
import cnpr.lcss.dao.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query("SELECT t.experience FROM Teacher t WHERE t.account.username = :username")
    String findExperienceByTeacherUsername(@Param("username") String teacherUsername);

    @Query("SELECT t.rating FROM Teacher t WHERE t.account.username = :username")
    String findRatingByTeacherUsername(@Param("username") String teacherUsername);

    @Query("SELECT tb.branch.branchId " +
            "FROM Teacher t " +
            "JOIN TeachingBranch tb ON t.teacherId = tb.teacher.teacherId " +
            "WHERE t.account.username = :username")
    int findBranchIdByTeacherUsername(@Param("username") String teacherUsername);

    @Query("SELECT b.branchName " +
            "FROM Branch b " +
            "JOIN TeachingBranch tb ON b.branchId = tb.branch.branchId " +
            "JOIN Teacher t ON t.teacherId = tb.teacher.teacherId " +
            "WHERE t.account.username = :username")
    String findBranchNameByTeacherUsername(@Param("username") String teacherUsername);

    Teacher findTeacherByAccount_Username(String teacherUsername);

    Teacher findByTeacherId(int teacherId);

    Page<Teacher> findByTeachingBranchList_Branch_BranchIdAndTeachingSubjectList_Subject_SubjectId(int branchId, int subjectId, Pageable pageable);

    Page<Teacher> findByTeachingSubjectList_Subject_SubjectId(int subjectId, Pageable pageable);

    Page<Teacher> findByTeachingBranchList_Branch_BranchId(int branchId, Pageable pageable);

    @Query(value = "SELECT t " +
            "FROM Teacher AS t ")
    Page<Teacher> findAllTeacher(Pageable pageable);

    @Query("select distinct t from Teacher t left join t.sessionList sessionList where sessionList.aClass.classId = ?1 and t.account.isAvailable = true")
    List<Teacher> findDistinctBySessionList_AClass_ClassIdAndAccount_IsAvailableIsTrue(int classId);

    @Query("select distinct t from Teacher t left join t.teachingBranchList teachingBranchList where teachingBranchList.branch.branchId = ?1 and t.account.isAvailable = ?2")
    Page<Teacher> findDistinctByTeachingBranchList_Branch_BranchIdAndAccount_IsAvailable(int branchId, boolean isAvailable, Pageable pageable);

    boolean existsByAccount_Username(String username);

    @Query("select distinct t from Teacher t left join t.teachingBranchList teachingBranchList where teachingBranchList.branch.branchId =?1 and t.account.isAvailable = ?2 and t.account.phone like %?3% and t.account.name like %?4%")
    Page<Teacher> findTeacherByBranch_BranchIdAndAccount_IsAvailableAndAccount_PhoneContainingIgnoreCaseAndAccount_NameContainingIgnoreCase(int branchId, boolean isAvailable,String phone, String name, Pageable pageable);
}