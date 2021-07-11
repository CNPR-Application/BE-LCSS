package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisteringGuestSearchResponseDto {
    private int id;
    private String customerName;
    private String phone;
    private String city;
    private Date bookingDate;
    private int curriculumId;
    private String curriculumName;
    private String description;
    private int branchId;
    private String status;
}
