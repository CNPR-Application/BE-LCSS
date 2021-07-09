package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Data
public class BookingRequestDto {
    private float payingPrice;
    private String description;
    private String status;
    private int studentId;
    private int branchId;
    private int subjectId;
    private int shiftId;
    private int classId;
}
