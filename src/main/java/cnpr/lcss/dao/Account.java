package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @Column(name = "role")
    private String role;
    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name = "creating_date")
    private Date creatingDate;

    @OneToOne(mappedBy = "account")
    private Staff staff;

    @OneToOne(mappedBy = "account")
    private Student student;

    @OneToOne(mappedBy = "account")
    private Teacher teacher;
}