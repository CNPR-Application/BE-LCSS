package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeachingSubjectDto {
    private Integer subjectId;
    private String subjectCode;
    private String subjectName;
}
