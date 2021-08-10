package cnpr.lcss.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Data
public class BookingRequestDto {
    private float payingPrice;
    private String description;
    private String status;
    private String studentUsername;
    private int branchId;
    private int classId;
}
