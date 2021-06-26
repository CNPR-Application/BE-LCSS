package cnpr.lcss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDto {

    private String username;
    private String password;
    private String name;
    private Date birthday;
    private String address;
    private String phone;
    private String email;
    private String image;
    private String role;
    private boolean isAvailable;
    private Date creatingDate;

    /**
     * --- modify getter & setter ---
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
