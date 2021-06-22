package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectDetailRequestDto {

    private int subjectId;
    private int weekNum;
    private String weekDescription;
    private String learningOutcome;
}
