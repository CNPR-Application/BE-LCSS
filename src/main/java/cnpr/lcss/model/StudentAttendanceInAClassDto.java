package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentAttendanceInAClassDto {
    private int attendanceId;
    private int sessionId;
    private String status;
    private Date startTime;
    private Date checkingDate;
    private int studentInClassId;
}
