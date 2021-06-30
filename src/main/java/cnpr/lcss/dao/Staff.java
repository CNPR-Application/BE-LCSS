package cnpr.lcss.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "staff")
public class Staff implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_username", referencedColumnName = "username")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public Staff(Account account, Branch branch) {
        this.account = account;
        this.branch = branch;
    }
}
