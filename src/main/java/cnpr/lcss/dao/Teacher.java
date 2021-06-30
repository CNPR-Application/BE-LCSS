package cnpr.lcss.dao;

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
@Table(name = "teacher")
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private int teacherId;
    @Column(name = "experience")
    private String experience;
    @Column(name = "rating")
    private String rating;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_username", referencedColumnName = "username")
    private Account account;

    @OneToMany(mappedBy = "teacher")
    private List<TeachingBranch> teachingBranchList;

    public Teacher(String experience, String rating, Account account) {
        this.experience = experience;
        this.rating = rating;
        this.account = account;
    }
}