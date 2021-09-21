package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionClassDto {
    private int sessionId;
    private int teacherId;
    private String teacherName;
    private String teacherImage;
    private int roomId;
    private int roomName;
    private Date startTime;
    private Date endTime;
}
