package cnpr.lcss.dao;

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
    private int attendanceId;
    @Column(name = "status")
    private String status;
    @Column(name = "creating_date")
    private Date creatingDate;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentInClass studentInClass;
}
