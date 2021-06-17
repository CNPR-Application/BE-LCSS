package cnpr.lcss.repository;

import cnpr.lcss.dao.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Page<Branch> findByBranchNameContainingIgnoreCaseAndIsAvailableIsTrue(String keyword, Pageable pageable);

    Branch findByBranchId(int branchId);

    Boolean existsBranchByBranchName(String branchName);

    Boolean existsBranchByBranchId(int branchId);

    Branch findOneByBranchId(int branchId);

    Boolean existsById(int branchId);
}