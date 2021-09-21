package cnpr.lcss.dao;

import cnpr.lcss.model.StudentAttendanceInAClassDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "attendance")
public class Attendance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer attendanceId;
    @Column(name = "status")
    private String status;
    @Column(name = "checking_date")
    private Date checkingDate;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentInClass studentInClass;

    //<editor-fold desc="Modify toString">
    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", status='" + status + '\'' +
                ", checkingDate=" + checkingDate +
                '}';
    }
    //</editor-fold>

    //<editor-fold desc="Convert to StudentAttendanceInAClassDto">
    public StudentAttendanceInAClassDto convertToStudentAttendanceInAClassDto() {
        StudentAttendanceInAClassDto dto = new StudentAttendanceInAClassDto();
        dto.setAttendanceId(attendanceId);
        dto.setSessionId(session.getSessionId());
        dto.setStatus(status);
        dto.setCheckingDate(checkingDate);
        dto.setStudentInClassId(studentInClass.getStudentInClassId());
        return dto;
    }
    //</editor-fold>
}