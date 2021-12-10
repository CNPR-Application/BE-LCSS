package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminStatisticInMonthResponse {
    private int branchId;
    private String branchName;
    private int newClass;
    private int newBooking;
    private int newRegisteredInfo;
    private int newStudent;
    private long newTeacher;
    private long monthRevenue;
    private int totalClass;
    private int totalBooking;
    private int totalRegisteredInfo;
    private int totalStudent;
    private int totalTeacher;
}
