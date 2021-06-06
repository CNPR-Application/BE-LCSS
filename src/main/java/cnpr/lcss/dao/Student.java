package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "student")
public class Student implements Serializable {

    @Id
    @Column(name = "student_username")
    private String studentUsername;

    @OneToOne
    @JoinColumn(name = "student_username")
    private Account account;

    @Column(name = "parent_phone")
    private String parentPhone;

    @Column(name = "parent_name")
    private String parentName;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

}
