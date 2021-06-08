package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurriculumResponseDto {

    private int curriculumId;
    private String curriculumCode;
    private String curriculumName;
    private String image;
}
