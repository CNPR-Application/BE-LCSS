package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserArrayInNotificationToGroupDto {
    private String username;
    private String name;
    private String bookingDate;
}
