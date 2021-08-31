package cnpr.lcss.dao;

import cnpr.lcss.model.BookingSearchResponseDto;
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
@Table(name = "booking")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingId;
    @Column(name = "paying_price")
    private float payingPrice;
    @Column(name = "paying_date")
    private Date payingDate;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "subject_id")
    private int subjectId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonIgnore
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonIgnore
    private Class aClass;

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    private List<StudentInClass> studentInClassList;

    //<editor-fold desc="Convert to SearchDto">
    public BookingSearchResponseDto convertToSearchDto() {
        BookingSearchResponseDto bookingSearchResponseDto = new BookingSearchResponseDto(
                bookingId,
                payingDate,
//        subject.getSubjectId(),
//        subject.getSubjectName(),
//        shift.getShiftId(),
//        shift.getDayOfWeek()+"-"+"("+shift.getTimeStart()+"-"+shift.getTimeEnd()+")",
                student.getId(),
                student.getAccount().getName(),
                student.getAccount().getImage(),
                status,
                branch.getBranchId(),
                branch.getBranchName(),
                payingPrice,
                description
        );
        return bookingSearchResponseDto;
    }
    //</editor-fold>
}
