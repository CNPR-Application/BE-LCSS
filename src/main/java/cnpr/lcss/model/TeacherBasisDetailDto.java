package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherBasisDetailDto {
    private Integer teacherId;
    private String teacherName;
    private String teacherUsername;
    private String teacherImage;
}
