package cnpr.lcss.service;


        import cnpr.lcss.dao.Branch;
        import cnpr.lcss.repository.BranchRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    // Search Branch by BranchId
    public List<Branch> getBranchByBranchId(int branchId) {

        return branchRepository.findByBranchId(branchId);
    }
}