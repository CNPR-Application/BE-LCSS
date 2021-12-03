package cnpr.lcss.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentInClassSuspendIsTrueOfStudent {
    private int classId;
    private int studentInClassId;
    private int subjectId;
    private Date openingDate;
    private Boolean suspend;
}
