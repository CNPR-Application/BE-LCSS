package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CurriculumRequestDto {
    
    private String curriculumCode;
    private String curriculumName;
    private String description;
    private String image;
    private String linkClip;
    private String learningOutcome;
}
