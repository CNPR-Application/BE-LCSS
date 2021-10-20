package cnpr.lcss.dao;

import cnpr.lcss.model.AllStudentAttendanceInASessionDto;
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
    @Column(name = "is_reopen")
    private Boolean isReopen;
    @Column(name = "closing_date")
    private Date closingDate;
    @Column(name = "reopen_reason")
    private String reopenReason;

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
                ", isReopen=" + isReopen +
                ", closingDate=" + closingDate +
                ", reopenReason='" + reopenReason + '\'' +
                '}';
    }
    //</editor-fold>

    //<editor-fold desc="Modify isReopen Getter & Setter">
    public Boolean getIsReopen() {
        return isReopen;
    }

    public void setIsReopen(Boolean isReopen) {
        this.isReopen = isReopen;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to StudentAttendanceInAClassDto">
    public StudentAttendanceInAClassDto convertToStudentAttendanceInAClassDto() {
        StudentAttendanceInAClassDto dto = new StudentAttendanceInAClassDto();
        dto.setAttendanceId(attendanceId);
        dto.setSessionId(session.getSessionId());
        dto.setStatus(status);
        dto.setStartTime(session.getStartTime());
        dto.setCheckingDate(checkingDate);
        dto.setStudentInClassId(studentInClass.getStudentInClassId());
        return dto;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to AllStudentAttendanceInASessionDto">
    public AllStudentAttendanceInASessionDto convertToAllStudentAttendanceInASessionDto() {
        AllStudentAttendanceInASessionDto dto = new AllStudentAttendanceInASessionDto();
        dto.setAttendanceId(attendanceId);
        dto.setCheckingDate(checkingDate);
        dto.setStatus(status);
        dto.setSessionId(session.getSessionId());
        dto.setStudentInClassId(studentInClass.getStudentInClassId());
        dto.setStudentUsername(studentInClass.getStudent().getAccount().getUsername());
        dto.setStudentName(studentInClass.getStudent().getAccount().getName());
        dto.setStudentImage(studentInClass.getStudent().getAccount().getImage());
        return dto;
    }
    //</editor-fold>
}