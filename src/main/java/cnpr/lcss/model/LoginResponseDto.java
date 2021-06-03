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
public class LoginResponseDto {

    private String name;
    private String address;
    private String email;
    private Date birthday;
    private String phone;
    private String image;
    private String role;
    private Date creatingDate;
}
