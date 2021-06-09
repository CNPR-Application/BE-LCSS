package cnpr.lcss.service;

import cnpr.lcss.dao.Branch;
import cnpr.lcss.errorMessageConfig.ErrorMessage;
import cnpr.lcss.model.BranchPagingResponseDto;
import cnpr.lcss.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    ErrorMessage errorMessage;

    // Find Branch by Branch Name LIKE keyword
    public BranchPagingResponseDto findByBranchNameContainingIgnoreCase(String keyword, int pageNo, int pageSize) {
        // pageNo starts at 0
        // always set first page = 1 ---> pageNo - 1
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Branch> branchList = branchRepository.findByBranchNameContainingIgnoreCase(keyword, pageable);

        BranchPagingResponseDto branchPagingResponseDto = new BranchPagingResponseDto(pageNo, pageSize, branchList);

        return branchPagingResponseDto;
    }

}
