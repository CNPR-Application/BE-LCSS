package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherScheduleDto {
    private Integer sessionId;
    private Integer classId;
    private String className;
    private Integer subjectId;
    private String subjectName;
    private Integer roomId;
    private String roomName;
    private Date startTime;
    private Date endTime;
}
