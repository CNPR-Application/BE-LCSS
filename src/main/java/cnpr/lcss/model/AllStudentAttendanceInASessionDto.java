package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllStudentAttendanceInASessionDto {
    private int attendanceId;
    private Date checkingDate;
    private String status;
    private Boolean isReopen;
    private Date closingDate;
    private String reopenReason;
    private int sessionId;
    private int studentInClassId;
    private String studentUsername;
    private String studentName;
    private String studentImage;
}
