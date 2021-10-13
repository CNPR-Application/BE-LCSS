package cnpr.lcss.dao;

import cnpr.lcss.model.StudentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "student")
public class Student implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "student_id")
    private Integer id;
    @Column(name = "parent_phone")
    private String parentPhone;
    @Column(name = "parent_name")
    private String parentName;

    @OneToOne
    @JoinColumn(name = "student_username", referencedColumnName = "username")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Booking> bookingList;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentInClass> studentInClassList;

    //<editor-fold desc="Convert to Dto">
    public StudentDto convertToDto(){
        StudentDto studentDto=new StudentDto();
        studentDto.setStudentId(id);
        studentDto.setName(account.getName());
        studentDto.setUsername(account.getUsername());
        studentDto.setAdress(account.getAddress());
        studentDto.setEmail(account.getEmail());
        studentDto.setBirthday(account.getBirthday());
        studentDto.setPhone(account.getPhone());
        studentDto.setImage(account.getImage());
        studentDto.setRole(account.getRole().getRoleId());
        studentDto.setParentName(parentName);
        studentDto.setParentPhone(parentPhone);
        return studentDto;
    }
    //</editor-fold>

    //<editor-fold desc="Modify toString">
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", parentPhone='" + parentPhone + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }
    //</editor-fold>


}
