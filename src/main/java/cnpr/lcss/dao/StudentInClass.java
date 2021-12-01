package cnpr.lcss.dao;

import cnpr.lcss.model.FeedbackDto;
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
    private double teacherRating;
    @Column(name = "subject_rating")
    private double subjectRating;
    @Column(name = "feedback")
    private String feedback;
    @Column(name = "suspend")
    private boolean suspend;

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
    public StudentInClass(Integer studentInClassId, double teacherRating, double subjectRating, String feedback) {
        this.studentInClassId = studentInClassId;
        this.teacherRating = teacherRating;
        this.subjectRating = subjectRating;
        this.feedback = feedback;
    }
    //</editor-fold>

    //<editor-fold desc="Modify toString()">
    @Override
    public String toString() {
        return "StudentInClass{" +
                "studentInClassId=" + studentInClassId +
                ", teacherRating=" + teacherRating +
                ", subjectRating=" + subjectRating +
                ", feedback='" + feedback + '\'' +
                '}';
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

    //<editor-fold desc="Convert to FeedbackDto">
    public FeedbackDto convertToFeedbackDto() {
        FeedbackDto dto = new FeedbackDto();
        dto.setStudentInClassId(studentInClassId);
        dto.setStudentId(student.getId());
        dto.setStudentUsername(student.getAccount().getUsername());
        dto.setStudentName(student.getAccount().getName());
        dto.setStudentImage(student.getAccount().getImage());
        dto.setTeacherRating(teacherRating);
        dto.setSubjectRating(subjectRating);
        dto.setFeedback(feedback);
        return dto;
    }
    //</editor-fold>
}