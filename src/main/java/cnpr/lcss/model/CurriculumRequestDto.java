package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurriculumRequestDto {

    private String curriculumCode;
    private String curriculumName;
    private String description;
    private String image;
    private String linkClip;
    private String learningOutcome;
}