package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisteringGuestRequestDto {

    private String customerName;
    private String phone;
    private String city;
    private String description;
    private int branchId;
    private int curriculumId;
}
