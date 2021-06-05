package cnpr.lcss.repository;

import cnpr.lcss.dao.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    @Query("SELECT s.branch.branchId FROM Staff s WHERE s.staffUsername = :username")
    int findBranchIdByStaffUsername(@Param("username") String staffUsername);

    @Query("SELECT s.branch.branchName FROM Staff s WHERE s.staffUsername = :username")
    String findBranchNameByStaffUsername(@Param("username") String staffUsername);
}
