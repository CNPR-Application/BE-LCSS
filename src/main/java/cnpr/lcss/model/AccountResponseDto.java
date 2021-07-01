package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountResponseDto {

    private String username;
    private String name;
    private String address;
    private String email;
    private Date birthday;
    private String phone;
    private String image;
    private String role;
    private Date creatingDate;
    private boolean isAvailable;
    private List<BranchResponseDto> branchResponseDtoList;
    private String parentPhone;
    private String parentName;
    private String experience;
    private String rating;

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
