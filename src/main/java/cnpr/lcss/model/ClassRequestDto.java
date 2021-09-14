package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassRequestDto {
    private String className;
    private Date openingDate;
    private int branchId;
    private int subjectId;
    private int shiftId;
    private String creator;
    private int roomName;
}
