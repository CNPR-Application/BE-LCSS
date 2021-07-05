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
@Table(name = "teaching_branch")
public class TeachingBranch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "starting_date")
    private Date startingDate;

    public TeachingBranch(Branch branch, Teacher teacher, Date startingDate) {
        this.branch = branch;
        this.teacher = teacher;
        this.startingDate = startingDate;
    }
}
