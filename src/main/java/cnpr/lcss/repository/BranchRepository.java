package cnpr.lcss.repository;

import cnpr.lcss.dao.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {
    //search Branch By ID
    Branch findByBranchId(int branchId);


}