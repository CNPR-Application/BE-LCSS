package cnpr.lcss.service;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.BranchRequestDto;
import cnpr.lcss.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    private final String DUPLICATE_NAME = "Duplicate Branch Name!";

    // Create new Curriculum
    @Transactional
    public ResponseEntity<?> createNewBranch(BranchRequestDto newBranch) throws Exception {

        try {
            if (branchRepository.existsBranchByBranchName(newBranch.getBranchName()) == Boolean.TRUE) {
                throw new Exception(DUPLICATE_NAME);
            } else {
                Branch createBranch = new Branch();

                createBranch.setBranchName(newBranch.getBranchName().trim());
                createBranch.setAddress(newBranch.getAddress().trim());
                createBranch.setOpeningDate(newBranch.getOpeningDate());
                createBranch.setIsAvailable(Boolean.TRUE);
                createBranch.setPhone(newBranch.getPhone().trim());

                branchRepository.save(createBranch);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
}
