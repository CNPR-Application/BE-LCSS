package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDto {

    private String name;
    private String address;
    private String email;
    private Date birthday;
    private String phone;
    private String image;
    private String role;
    private Date creatingDate;
    // role: Staff, Manager
    private int branchId;
    private String branchName;
    // role: Student
    private String parentPhone;
    private String parentName;
    // role: Teacher
    private String experience;
    private String rating;
}
