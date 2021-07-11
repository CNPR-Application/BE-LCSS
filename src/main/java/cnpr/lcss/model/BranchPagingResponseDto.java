package cnpr.lcss.model;

import cnpr.lcss.dao.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BranchPagingResponseDto {

    private int pageNo;
    private int pageSize;
    private int pageTotal;
    private List<Branch> branchResponseDtos;
}
