package cnpr.lcss.repository;

import cnpr.lcss.dao.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Boolean existsBranchByBranchName(String branchName);
}
