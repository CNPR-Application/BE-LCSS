package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingSearchResponseDto {
    private int bookingId;
    private Date payingDate;
    private int classId;
    private String className;
    private int shiftId;
    private String shiftDescription;
    private int subjectId;
    private String subjectName;
    private int studentId;
    private String studentName;
    private String studentUsername;
    private String image;
    private String status;
    private int branchId;
    private String branchName;
    private float payingPrice;
    private String description;
}
