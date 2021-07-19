package cnpr.lcss.dao;

import cnpr.lcss.model.StudentInClassSearchResponseDto;
import cnpr.lcss.model.SubjectSearchDto;
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
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public StudentInClassSearchResponseDto convertToSearchDto() {
        StudentInClassSearchResponseDto studentInClassSearchResponseDto = new StudentInClassSearchResponseDto(
         aClass.getClassId(),
        student.getId(),
        student.getAccount().getUsername(),
       student.getAccount().getName(),
         student.getAccount().getImage(),
         booking.getBookingId(),
        booking.getPayingDate());
                return studentInClassSearchResponseDto;
    }
}