package cnpr.lcss.service;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    private final String BRANCH_ID_DOES_NOT_EXIST = "Branch Id does not exist!";

    // Delete Branch by Branch Id
    // Change isAvailable from True to False
    public ResponseEntity<?> deleteByBranchId(int branchId) throws Exception {
        try {
            if (!branchRepository.existsBranchByBranchId(branchId)) {
                throw new IllegalArgumentException(BRANCH_ID_DOES_NOT_EXIST);
            } else {
                Branch delCur = branchRepository.findOneByBranchId(branchId);
                if (delCur.getIsAvailable().equals(Boolean.TRUE)) {
                    delCur.setIsAvailable(Boolean.FALSE);
                    branchRepository.save(delCur);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.ok(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}