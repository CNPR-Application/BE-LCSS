package cnpr.lcss.repository;

import cnpr.lcss.dao.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {

    Branch findByBranchId(int branchId);

    Boolean existsBranchByBranchId(int branchId);

    Branch findOneByBranchId(int branchId);
}