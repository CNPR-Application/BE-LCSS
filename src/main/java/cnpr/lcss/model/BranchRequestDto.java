package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BranchRequestDto {

    private String branchName;
    private String address;
    private Date openingDate;
    private String phone;
}
