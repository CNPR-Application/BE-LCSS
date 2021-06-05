package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teacher")
public class Teacher implements Serializable {

    @Id
    @Column(name = "teacher_username")
    private String teacherUsername;
    @Column(name = "experience")
    private String experience;
    @Column(name = "rating")
    private String rating;

    @OneToOne
    @JoinColumn(name = "username")
    private Account account;

    @OneToMany(mappedBy = "teacher")
    private List<TeachingBranch> teachingBranchList;
}
