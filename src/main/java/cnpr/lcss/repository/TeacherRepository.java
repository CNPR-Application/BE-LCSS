package cnpr.lcss.repository;

import cnpr.lcss.dao.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {

    @Query("SELECT t.experience FROM Teacher t WHERE t.teacherUsername = :username")
    String findExperienceByTeacherUsername(@Param("username") String teacherUsername);

    @Query("SELECT t.rating FROM Teacher t WHERE t.teacherUsername = :username")
    String findRatingByTeacherUsername(@Param("username") String teacherUsername);

    @Query("SELECT tb.branch.branchId FROM Teacher t " +
            "JOIN TeachingBranch tb ON t.teacherUsername = tb.teacher.teacherUsername " +
            "WHERE t.teacherUsername = :username")
    int findBranchIdByTeacherUsername(@Param("username") String teacherUsername);

    @Query("SELECT b.branchName FROM Branch b " +
            "JOIN TeachingBranch tb ON b.branchId = tb.branch.branchId" +
            " WHERE tb.teacher.teacherUsername = :username")
    String findBranchNameByTeacherUsername(@Param("username") String teacherUsername);

    Teacher findTeacherByTeacherUsername(String teacherUsername);
}
