package cnpr.lcss.repository;
import cnpr.lcss.dao.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface BranchRepository extends JpaRepository<Branch, String> {

    Branch findByBranchId(int branchId);
}
