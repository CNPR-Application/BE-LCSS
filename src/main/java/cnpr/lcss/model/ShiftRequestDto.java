package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShiftRequestDto {

    private String dayOfWeek;
    private String timeStart;
    private int duration;
}