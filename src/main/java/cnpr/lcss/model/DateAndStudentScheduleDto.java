package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DateAndStudentScheduleDto {
    Date datetime;
    List<StudentScheduleDto> studentSessionList;
}
