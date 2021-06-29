package cnpr.lcss.repository;

import cnpr.lcss.dao.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("SELECT s.parentName FROM Student s WHERE s.account.username = :username")
    String findParentNameByStudentUsername(@Param("username") String studentUsername);

    @Query("SELECT s.parentPhone FROM Student s WHERE s.account.username = :username")
    String findParentPhoneByStudentUsername(@Param("username") String studentUsername);

    @Query("SELECT s.branch.branchId FROM Student s JOIN Account a ON s.account.username = a.username WHERE a.username = :username")
    int findBranchIdByStudentUsername(@Param("username") String studentUsername);

    @Query("SELECT b.branchName FROM Branch b JOIN Student s ON b.branchId = s.branch.branchId WHERE s.account.username = :username")
    String findBranchNameByStudentUsername(@Param("username") String studentUsername);

    Student findStudentByAccount_Username(String studentUsername);
}
