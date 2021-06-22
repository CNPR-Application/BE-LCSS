package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
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
