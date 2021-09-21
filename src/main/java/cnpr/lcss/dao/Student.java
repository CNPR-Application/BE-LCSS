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
