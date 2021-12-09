package cnpr.lcss.repository;

import cnpr.lcss.dao.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Query("SELECT s.branch.branchId " +
            "FROM Staff AS s " +
            "WHERE s.account.username = :username")
    Integer findBranchIdByStaffUsername(@Param("username") String staffUsername);

    @Query("SELECT s.branch.branchName " +
            "FROM Staff AS s " +
            "WHERE s.account.username = :username")
    String findBranchNameByStaffUsername(@Param("username") String staffUsername);

    Staff findByAccount_Username(String staffUsername);

    boolean existsByAccount_Role_RoleIdAndBranch_BranchId(String roleId, int branchId);
}
