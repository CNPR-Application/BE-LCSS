package cnpr.lcss.service;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.BranchPagingResponseDto;
import cnpr.lcss.model.BranchRequestDto;
import cnpr.lcss.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    private final String DUPLICATE_NAME = "Duplicate Branch Name!";
    private final String BRANCH_ID_DOES_NOT_EXIST = "Branch Id does not exist!";

    // Find Branch by Branch Name LIKE keyword
    public BranchPagingResponseDto findByBranchNameContainingIgnoreCase(String keyword, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Branch> branchList = branchRepository.findByBranchNameContainingIgnoreCase(keyword, pageable);

        BranchPagingResponseDto branchPagingResponseDto = new BranchPagingResponseDto(pageNo, pageSize, branchList);

        return branchPagingResponseDto;
    }

    // Search Branch by BranchId
    public Branch findBranchByBranchId(int branchId) {
        return branchRepository.findByBranchId(branchId);
    }

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