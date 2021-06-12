package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BranchRequestDto {

    private String branchName;
    private String address;
    private Boolean isAvailable;
    private Date openingDate;
    private String phone;
}
