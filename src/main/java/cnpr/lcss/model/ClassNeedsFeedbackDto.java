package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassNeedsFeedbackDto {
    private int classId;
    private String className;
    private Date classOpeningDate;
    private String classStatus;
    private int classSlot;
    private int subjectId;
    private int branchId;
    private Integer teacherId;
    private Integer studentInClassId;
}
