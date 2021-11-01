package cnpr.lcss.dao;

import cnpr.lcss.model.AccountByRoleDto;
import cnpr.lcss.model.AccountDto;
import cnpr.lcss.model.BookingSearchResponseDto;
import cnpr.lcss.model.TeacherInBranchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "image")
    private String image;
    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name = "creating_date")
    private Date creatingDate;
    @Column(name = "token")
    private String token;

    @OneToOne(mappedBy = "account")
    private Staff staff;
    @OneToOne(mappedBy = "account")
    private Student student;
    @OneToOne(mappedBy = "account")
    private Teacher teacher;

    @OneToMany(mappedBy = "receiverUsername")
    private List<Notification> notificationList;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    //<editor-fold desc="Modify isAvailable">
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    //</editor-fold>

    //<editor-fold desc="Modify toString()">
    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", isAvailable=" + isAvailable +
                ", creatingDate=" + creatingDate +
                ", token='" + token + '\'' +
                '}';
    }
    //</editor-fold>
    //<editor-fold desc="Convert To Account By Role DTO">
    public AccountByRoleDto convertToAccountDto() {
        AccountByRoleDto accountDto = new AccountByRoleDto();
        accountDto.setUsername(username);
        accountDto.setName(name);
        return accountDto;
    }
    //</editor-fold>

}