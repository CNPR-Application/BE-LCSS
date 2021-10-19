package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentScheduleDto {
    private Integer sessionId;
    private Integer classId;
    private String className;
    private Integer subjectId;
    private String subjectName;
    private Integer teacherId;
    private String teacherName;
    private String teacherImage;
    private Integer roomName;
    private Date startTime;
    private Date endTime;
}
