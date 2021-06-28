package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDto {

    private int id;
    private String parentPhone;
    private String parentName;
    private String studentUsername;
    private int branchId;
}
