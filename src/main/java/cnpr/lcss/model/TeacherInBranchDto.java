package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherInBranchDto {
    private int teacherId;
    private String teacherUsername;
    private String teacherName;
    private String teacherAddress;
    private String teacherEmail;
    private Date teacherBirthday;
    private String teacherPhone;
    private String teacherImage;
    private String role;
    private Date accountCreatingDate;
    private Date teacherStartingDate;
    private String teacherExperience;
    private String teacherRating;
    private List<SubjectBasicInfoDto> teachingSubjectList;
}
