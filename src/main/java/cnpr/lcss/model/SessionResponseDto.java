package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionResponseDto {
    private int sessionId;
    private int classId;
    private String className;
    private int subjectId;
    private String subjectName;
    private int teacherId;
    private String teacherName;
    private String teacherImage;
    private int roomId;
    private int roomName;
    private Date startTime;
    private Date endTime;
}
