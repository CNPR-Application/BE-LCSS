package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackDto {
    private int studentInClassId;
    private int studentId;
    private String studentUsername;
    private String studentName;
    private String studentImage;
    private int teacherRating;
    private int subjectRating;
    private String feedback;
}
