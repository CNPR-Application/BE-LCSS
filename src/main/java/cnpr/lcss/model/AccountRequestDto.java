package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountRequestDto {

    private String name;
    private String address;
    private String email;
    private Date birthday;
    private String phone;
    private String image;
    private int branchId;
    private String parentName;
    private String parentPhone;
    private String experience;
}
