package cnpr.lcss.dao;

import cnpr.lcss.model.SessionResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "session")
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private int sessionId;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private List<Attendance> attendanceList;

    //<editor-fold desc="Convert to SessionResponseDto">
    public SessionResponseDto convertToSessionResponseDto() {
        SessionResponseDto dto = new SessionResponseDto();
        dto.setSessionId(sessionId);
        dto.setClassId(aClass.getClassId());
        dto.setClassName(aClass.getClassName());
        dto.setSubjectId(aClass.getSubject().getSubjectId());
        dto.setSubjectName(aClass.getSubject().getSubjectName());
        dto.setTeacherId(teacher.getTeacherId());
        dto.setTeacherName(teacher.getAccount().getName());
        dto.setTeacherImage(teacher.getAccount().getImage());
        dto.setRoomNo(room.getRoomName());
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        return dto;
    }
    //</editor-fold>
}
