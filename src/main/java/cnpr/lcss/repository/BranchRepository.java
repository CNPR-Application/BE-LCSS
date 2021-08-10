package cnpr.lcss.repository;

import cnpr.lcss.dao.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    @Query(value = "SELECT new Branch(b.branchId, b.branchName,b.address,b.isAvailable,b.openingDate,b.phone) " +
            "FROM Branch b " +
            "WHERE b.branchName like %:keyword%  AND b.isAvailable=:isAvailable")
    Page<Branch> findByBranchNameContainingIgnoreCaseAndIsAvailable(String keyword, boolean isAvailable, Pageable pageable);

    Branch findByBranchId(int branchId);

    Boolean existsBranchByBranchName(String branchName);

    Boolean existsBranchByBranchId(int branchId);

    Branch findOneByBranchId(int branchId);

    Boolean existsById(int branchId);

    @Query(value = "SELECT new Branch(b.branchId, b.branchName) " +
            "FROM Branch b " +
            "JOIN TeachingBranch tb on b.branchId = tb.branch.branchId " +
            "JOIN Teacher t on t.teacherId = tb.teacher.teacherId " +
            "WHERE t.account.username = :username")
    List<Branch> findTeacherBranchByAccountUsername(@Param(value = "username") String username);

    @Query(value = "SELECT new Branch(b.branchId, b.branchName) " +
            "FROM Branch b " +
            "JOIN Staff s ON s.branch.branchId = b.branchId " +
            "WHERE s.account.username = :username")
    List<Branch> findStaffBranchByAccountUsername(@Param(value = "username") String username);

    @Query(value = "SELECT new Branch(b.branchId, b.branchName) " +
            "FROM Branch b " +
            "JOIN Student s ON s.branch.branchId = b.branchId " +
            "WHERE s.account.username = :username")
    List<Branch> findStudentBranchByAccountUsername(@Param(value = "username") String username);

    @Query(value = "SELECT b.isAvailable " +
            "FROM Branch AS b " +
            "WHERE b.branchId = :branchId")
    boolean findIsAvailableByBranchId(@Param(value = "branchId") int branchId);

    @Query(value = "SELECT b.branchName " +
            "FROM Branch AS b " +
            "WHERE b.branchId = :branchId")
    String findBranch_BranchNameByBranchId(@Param(value = "branchId") int branchId);
}