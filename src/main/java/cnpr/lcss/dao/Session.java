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
    @Column(name = "room_no")
    private int roomNo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private Class aClass;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Attendance> attendanceList;

    //<editor-fold desc="Convert to SessionResponseDto">
    public SessionResponseDto convertToSessionResponseDto() {
        SessionResponseDto sessionResponseDto = new SessionResponseDto(
                sessionId,
                aClass.getClassId(),
                aClass.getClassName(),
                aClass.getSubject().getSubjectId(),
                aClass.getSubject().getSubjectName(),
                teacher.getTeacherId(),
                teacher.getAccount().getName(),
                teacher.getAccount().getImage(),
                roomNo,
                startTime,
                endTime
        );
        return sessionResponseDto;
    }
    //</editor-fold>
}
