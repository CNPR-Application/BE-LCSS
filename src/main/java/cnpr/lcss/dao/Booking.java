package cnpr.lcss.dao;

import cnpr.lcss.model.BookingSearchResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    //<editor-fold desc="Convert to SearchDto">
    public BookingSearchResponseDto convertToSearchDto() {
        BookingSearchResponseDto bookingSearchResponseDto = new BookingSearchResponseDto();
        bookingSearchResponseDto.setBookingId(bookingId);
        bookingSearchResponseDto.setPayingDate(payingDate);
        bookingSearchResponseDto.setClassId(aClass.getClassId());
        bookingSearchResponseDto.setClassName(aClass.getClassName());
        bookingSearchResponseDto.setShiftId(aClass.getShift().getShiftId());
        bookingSearchResponseDto.setShiftDescription(aClass.getShift().getDayOfWeek() + "-" + "(" + aClass.getShift().getTimeStart() + "-" + aClass.getShift().getTimeEnd() + ")");
        bookingSearchResponseDto.setSubjectId(subjectId);
        //subjectName
        bookingSearchResponseDto.setStudentId(student.getId());
        bookingSearchResponseDto.setStudentName(student.getAccount().getName());
        bookingSearchResponseDto.setImage(student.getAccount().getImage());
        bookingSearchResponseDto.setStatus(status);
        bookingSearchResponseDto.setBranchId(branch.getBranchId());
        bookingSearchResponseDto.setBranchName(branch.getBranchName());
        bookingSearchResponseDto.setPayingPrice(payingPrice);
        bookingSearchResponseDto.setDescription(description);
        return bookingSearchResponseDto;
    }
    //</editor-fold>

    //<editor-fold desc="Modify toString()">
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", payingPrice=" + payingPrice +
                ", payingDate=" + payingDate +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", subjectId=" + subjectId +
                '}';
    }
    //</editor-fold>
}
