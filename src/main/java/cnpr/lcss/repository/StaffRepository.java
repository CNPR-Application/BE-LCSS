package cnpr.lcss.repository;

import cnpr.lcss.dao.Staff;
import cnpr.lcss.model.StaffDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    @Query("SELECT s.branch.branchId " +
            "FROM Staff AS s " +
            "WHERE s.account.username = :username")
    Integer findBranchIdByStaffUsername(@Param("username") String staffUsername);

    @Query("SELECT s.branch.branchName " +
            "FROM Staff AS s " +
            "WHERE s.account.username = :username")
    String findBranchNameByStaffUsername(@Param("username") String staffUsername);

    Staff findByAccount_Username(String staffUsername);
}
