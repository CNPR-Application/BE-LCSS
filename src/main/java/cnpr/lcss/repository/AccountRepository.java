package cnpr.lcss.repository;

import cnpr.lcss.dao.Account;
import cnpr.lcss.dao.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("select count(a) from Account a where a.role.roleId = ?1")
    long countByRole_RoleId(String roleId);

    Account findOneByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT a.role " +
            "FROM Account a " +
            "WHERE a.username = :username")
    Role findRoleByUsername(@Param(value = "username") String username);

    Page<Account> findByRole_RoleIdAndUsernameContainingAndIsAvailable(@Param(value = "role") String role,
                                                                       @Param(value = "username") String keyword,
                                                                       @Param(value = "isAvailable") boolean isAvailable,
                                                                       Pageable pageable);

    Page<Account> findByRole_RoleIdAndNameContainingIgnoreCase(String role, String keyword, Pageable pageable);

    @Query("select distinct a " +
            "from Account a " +
            "where a.isAvailable = true " +
            "and a.role.roleId not in ?1 " +
            "and a.staff.branch.branchId = ?2")
    List<Account> findAvailableStaffAndManagerByBranchId(String roleId, int branchId);

    @Query("select distinct a " +
            "from Account a " +
            "left join a.teacher.teachingBranchList teachingBranchList " +
            "where a.isAvailable = true " +
            "and a.role.roleId = ?1 " +
            "and teachingBranchList.branch.branchId = ?2")
    List<Account> findAvailableTeacherByBranchId(String roleId, int branchId);

    @Query("select distinct a " +
            "from Account a " +
            "where a.isAvailable = true " +
            "and a.role.roleId = ?1 " +
            "and a.student.branch.branchId = ?2")
    List<Account> findAvailableStudentByBranchId(String roleId, int branchId);

    List<Account> findAllByStaff_Branch_BranchIdAndRole_RoleIdAndIsAvailable(int branchId, String role, boolean isAvailable);

    @Query(nativeQuery = true,
            value = "select a.* " +
                    "from account as a " +
                    "join student s on a.username = s.student_username " +
                    "join student_in_class sic on s.student_id = sic.student_id " +
                    "where sic.student_class_id = ?1")
    Account findByStudentInClass_Id(Integer id);

    int countAccountByUsernameIsInAndCreatingDateIsGreaterThanEqualAndRole_RoleId(List<String> username, Date date, String role);
}