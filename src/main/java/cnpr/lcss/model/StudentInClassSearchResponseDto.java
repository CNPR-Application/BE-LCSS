package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentInClassSearchResponseDto {
    private int classId;
    private int studentId;
    private String studentUserName;
    private String studentName;
    private String  image;
    private int bookingId;
    private Date payingDate;

}
