package cnpr.lcss.dao;

import cnpr.lcss.model.StudentInClassSearchResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "student_in_class")
public class StudentInClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_class_id")
    private Integer studentInClassId;
    @Column(name = "teacher_rating")
    private int teacherRating;
    @Column(name = "subject_rating")
    private int subjectRating;
    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "studentInClass")
    @JsonIgnore
    private List<Attendance> attendanceList;

    //<editor-fold desc="Modify Constructor">
    public StudentInClass(Integer studentInClassId, int teacherRating, int subjectRating, String feedback) {
        this.studentInClassId = studentInClassId;
        this.teacherRating = teacherRating;
        this.subjectRating = subjectRating;
        this.feedback = feedback;
    }
    //</editor-fold>

    //<editor-fold desc="Convert to SearchDto">
    public StudentInClassSearchResponseDto convertToSearchDto() {
        StudentInClassSearchResponseDto dto = new StudentInClassSearchResponseDto();
        dto.setClassId(aClass.getClassId());
        dto.setStudentId(student.getId());
        dto.setStudentUserName(student.getAccount().getUsername());
        dto.setStudentName(student.getAccount().getName());
        dto.setImage(student.getAccount().getImage());
        return dto;
    }

    //</editor-fold>
}