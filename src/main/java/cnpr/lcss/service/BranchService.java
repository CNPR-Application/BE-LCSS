package cnpr.lcss.service;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;


    public Boolean removeBranch(int branchId) {

        Boolean result = false;
        Branch isExistedBranch;
        String checkId=String.valueOf(branchId);
        try {

            if (checkId == null || checkId.isEmpty()) {
                result = false;
                throw new IllegalArgumentException();
            } else {

                isExistedBranch = branchRepository.findByBranchId(branchId);


                if (isExistedBranch!=null) {
                    isExistedBranch.setIsAvailable(false);
                    branchRepository.save(isExistedBranch);
                    result = true;
                }

            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }


}