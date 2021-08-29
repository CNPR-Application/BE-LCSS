package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherDto {
    private Integer teacherId;
    private String username;
    private String experience;
    private String rating;
    private String name;
    private String address;
    private String email;
    private Date birthday;
    private String phone;
    private String image;
    private String role;
    private Date creatingDate;
    private List<TeachingBranchBasicInfoDto> teachingBranchBasicInfoDtoList;
    private List<TeachingSubjectBasicInfoDto> teachingSubjectBasicInfoDtoList;
}
