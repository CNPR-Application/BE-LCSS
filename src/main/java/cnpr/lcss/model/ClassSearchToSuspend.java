package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassSearchToSuspend {
    private int classId;
    private String className;
    private Date openingDate;
    private String status;
    private int slot;
    private int subjectId;
    private String subjectName;
    private int shiftId;
    private String shiftDescription;
    private String roomName;
    private Integer roomId;
}
