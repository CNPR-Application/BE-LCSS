package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShiftDto {

    private int shiftId;
    private String timeStart;
    private String timeEnd;
    private String dayOfWeek;
    private int duration;
    private boolean isAvailable;
}
