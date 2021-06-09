package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurriculumResponseDto {

    private int curriculumId;
    private String curriculumCode;
    private String curriculumName;
    private String description;
    private Date creatingDate;
    private Boolean isAvailable;
    private String image;
    private String linkClip;
    private String learningOutcome;
}
