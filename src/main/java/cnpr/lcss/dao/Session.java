package cnpr.lcss.dao;

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
    @Column(name = "teacher_id")
    private int teacherId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private Class aClass;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Attendance> attendanceList;

    public Session(int sessionId, Date startTime, Date endTime, int roomNo, int teacherId) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNo = roomNo;
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", roomNo=" + roomNo +
                ", teacherId=" + teacherId +
                '}';
    }
}
