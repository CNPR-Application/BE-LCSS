package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassTeacherSearchDto {
    private int classId;
    private String className;
    private Date openingDate;
    private String status;
    private int slot;
    private int subjectId;
    private String subjectName;
    private float subjectPrice;
    private int branchId;
    private String branchName;
    private int shiftId;
    private String shiftDescription;
    private int roomName;
    private Integer roomId;
}
