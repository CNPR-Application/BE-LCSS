package cnpr.lcss.service;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.model.BranchPagingResponseDto;
import cnpr.lcss.model.BranchRequestDto;
import cnpr.lcss.repository.BranchRepository;
import cnpr.lcss.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    //<editor-fold desc="Find Branch by Branch Name LIKE keyword And Is Available (true/false)">
    public BranchPagingResponseDto findByBranchNameContainingIgnoreCaseAndIsAvailableIsTrue(String keyword, boolean isAvailable, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Branch> page = branchRepository.findByBranchNameContainingIgnoreCaseAndIsAvailable(keyword, isAvailable, pageable);
        List<Branch> branchList = page.getContent();
        int pageTotal = page.getTotalPages();

        BranchPagingResponseDto branchPagingResponseDto = new BranchPagingResponseDto(pageNo, pageSize, pageTotal, branchList);

        return branchPagingResponseDto;
    }
    //</editor-fold>

    //<editor-fold desc="Search Branch by BranchId">
    public Branch findBranchByBranchId(int branchId) {
        return branchRepository.findByBranchId(branchId);
    }
    //</editor-fold>

    //<editor-fold desc="Create new Curriculum">
    @Transactional
    public ResponseEntity<?> createNewBranch(BranchRequestDto newBranch) throws Exception {

        try {
            if (branchRepository.existsBranchByBranchName(newBranch.getBranchName()) == Boolean.TRUE) {
                throw new Exception(Constant.DUPLICATE_BRANCH_NAME);
            } else {
                Branch createBranch = new Branch();

                createBranch.setBranchName(newBranch.getBranchName().trim());
                createBranch.setAddress(newBranch.getAddress().trim());
                createBranch.setOpeningDate(newBranch.getOpeningDate());
                createBranch.setIsAvailable(Boolean.TRUE);
                if (!newBranch.getPhone().matches(Constant.PHONE_PATTERN)) {
                    throw new Exception(Constant.INVALID_PHONE_PATTERN);
                } else {
                    createBranch.setPhone(newBranch.getPhone().trim());
                }

                branchRepository.save(createBranch);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Delete Branch by Branch Id">
    public ResponseEntity<?> deleteByBranchId(int branchId) throws Exception {
        try {
            if (!branchRepository.existsBranchByBranchId(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
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
    //</editor-fold>

    //<editor-fold desc="Update Branch by Branch id">
    public ResponseEntity<?> updateBranch(int branchId, BranchRequestDto insBranch) throws Exception {
        try {
            if (!branchRepository.existsById(branchId)) {
                throw new IllegalArgumentException(Constant.INVALID_BRANCH_ID);
            } else {
                Branch updateBranch = branchRepository.findOneByBranchId(branchId);

                updateBranch.setBranchName(insBranch.getBranchName().trim());
                updateBranch.setAddress(insBranch.getAddress().trim());
                //EDIT BRANCH DATE
                updateBranch.setOpeningDate(insBranch.getOpeningDate());
                //ADMIN now can edit branch isAvailable
                updateBranch.setIsAvailable(insBranch.getIsAvailable());
                if (!insBranch.getPhone().matches(Constant.PHONE_PATTERN)) {
                    throw new Exception(Constant.INVALID_PHONE_PATTERN);
                } else {
                    updateBranch.setPhone(insBranch.getPhone().trim());
                }

                branchRepository.save(updateBranch);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }
    //</editor-fold>
}