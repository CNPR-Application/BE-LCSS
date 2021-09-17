package cnpr.lcss.dao;

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
    private int id;
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

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", parentPhone='" + parentPhone + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}
