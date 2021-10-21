package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {
    private int studentId;
    private String name;
    private String username;
    private String address;
    private String email;
    private Date birthday;
    private String phone;
    private String image;
    private String role;
    private String parentName;
    private String parentPhone;

}
